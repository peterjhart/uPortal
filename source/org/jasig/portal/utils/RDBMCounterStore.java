/**
 * Copyright � 2001 The JA-SIG Collaborative.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the JA-SIG Collaborative
 *    (http://www.jasig.org/)."
 *
 * THIS SOFTWARE IS PROVIDED BY THE JA-SIG COLLABORATIVE "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JA-SIG COLLABORATIVE OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */


package org.jasig.portal.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jasig.portal.PortalException;
import org.jasig.portal.RDBMServices;
import org.jasig.portal.services.LogService;

/**
 * A reference implementation for the counter store
 *
 * @author George Lindholm, george.lindholm@ubc.ca
 * @author <a href="mailto:pkharchenko@interactivebusiness.com">Peter Kharchenko</a>
 * @author Eric Dalquist <a href="mailto:edalquist@unicon.net">edalquist@unicon.net</a>
 * @version $Revision$
 */
public class RDBMCounterStore implements ICounterStore {
    
    /**
     * Creates a new counter with an initial value of 0. Does not check to
     * see if the counter already exists.
     * 
     * @see org.jasig.portal.utils.ICounterStore#createCounter(java.lang.String)
     */
    public synchronized void createCounter (String counterName) throws Exception {
        Connection con = RDBMServices.getConnection();
        PreparedStatement createCounterPstmt = null;
        
        try {
            RDBMServices.setAutoCommit(con, false);
            
            String createCounterInsert =
                "INSERT INTO UP_SEQUENCE (SEQUENCE_NAME, SEQUENCE_VALUE) " +
                "VALUES (?, 0)";
            
            createCounterPstmt = con.prepareStatement(createCounterInsert);
            createCounterPstmt.setString(1, counterName);
            
            LogService.log(LogService.DEBUG, "RDBMCounterStore::createCounter(" + counterName + "): " + createCounterInsert);
            int updateCount = createCounterPstmt.executeUpdate();
            
            if (updateCount <= 0) {
                PortalException pe = new PortalException("RDBMCounterStore::createCounter(): An error occured while creating the counter named: " + counterName + ".\nNo rows were created.");
                LogService.log(LogService.ERROR, pe); 
                throw pe;
            }
            
            RDBMServices.commit(con);
        }
        catch (SQLException sqle) {
            RDBMServices.rollback(con);
            
            PortalException pe = new PortalException("RDBMCounterStore::createCounter(): An error occured while creating the counter named: " + counterName, sqle);
            LogService.log(LogService.ERROR, pe); 
            throw pe;
        } 
        finally {
            try { createCounterPstmt.close(); } catch (Exception e) {};
       
            RDBMServices.releaseConnection(con);
        }
    }


    /**
     * Sets the counter to the specified value. Does not check to make
     * sure the counter already exists.
     * 
     * @see org.jasig.portal.utils.ICounterStore#setCounter(java.lang.String, int)
     */
    public synchronized void setCounter (String counterName, int value) throws Exception {
        Connection con = RDBMServices.getConnection();
        
        PreparedStatement setCounterPstmt = null;
        
        try {
            RDBMServices.setAutoCommit(con, false);
            
            String setCounterUpdate = 
                "UPDATE UP_SEQUENCE " +
                "SET SEQUENCE_VALUE=? " +
                "WHERE SEQUENCE_NAME=?";
            
            setCounterPstmt = con.prepareStatement(setCounterUpdate);
            setCounterPstmt.setInt(1, value);
            setCounterPstmt.setString(2, counterName);
            
            LogService.log(LogService.DEBUG, "RDBMCounterStore::setCounter(" + counterName + ", " + value + "): " + setCounterUpdate);
            int updateCount = setCounterPstmt.executeUpdate();
            
            if (updateCount <= 0) {
                PortalException pe = new PortalException("RDBMCounterStore::setCounter(): An error occured while setting the counter named: " + counterName + ".\nNo rows were updated.");
                LogService.log(LogService.ERROR, pe); 
                throw pe;
            }            
            
            RDBMServices.commit(con);
        } 
        catch (SQLException sqle) {
            RDBMServices.rollback(con);
            
            PortalException pe = new PortalException("RDBMCounterStore::createCounter(): An error occured while creating the counter named: " + counterName, sqle);
            LogService.log(LogService.ERROR, pe); 
            throw pe;
            
        }
        finally {
            try { setCounterPstmt.close(); } catch (Exception e) {};
            
            RDBMServices.releaseConnection(con);
        }
    }

    /**
     * Gets the next number in the sequence. If the counter does not exist
     * it is first created.
     * 
     * @see org.jasig.portal.utils.ICounterStore#getIncrementIntegerId(java.lang.String)
     */
    public synchronized int getIncrementIntegerId (String counterName) throws Exception {
        Connection con = RDBMServices.getConnection();
        
        PreparedStatement getCounterPstmt = null;
        PreparedStatement updateCounterPstmt = null;
        ResultSet rs = null;
        
        try {
            String getCounterQuery =
                "SELECT SEQUENCE_VALUE " +
                "FROM UP_SEQUENCE " +
                "WHERE SEQUENCE_NAME=?";
            
            String updateCounterQuery =
                "UPDATE UP_SEQUENCE " +
                "SET SEQUENCE_VALUE=? " +
                "WHERE SEQUENCE_NAME=? AND SEQUENCE_VALUE=?";
            
            getCounterPstmt = con.prepareStatement(getCounterQuery);
            getCounterPstmt.setString(1, counterName);
            
            updateCounterPstmt = con.prepareStatement(updateCounterQuery);
            updateCounterPstmt.setString(2, counterName);
            
            for (int i = 0; i < 25; i++) 
            {
                LogService.log(LogService.DEBUG, "RDBMCounterStore::getIncrementInteger(" + counterName + "): " + getCounterQuery);
                rs = getCounterPstmt.executeQuery();
                
                if (!rs.next()) {
                    try {
                        createCounter(counterName);
                    }
                    catch (Exception e) {
                        throw new PortalException("RDBMCounterStore::getIncrementInteger(): Could not create new counter for name: " + counterName, e);
                    }
                    
                    rs = getCounterPstmt.executeQuery();
                    
                    if (!rs.next()) {
                        throw new PortalException("RDBMCounterStore::getIncrementInteger(): Counter should have been created but was not found, name: " + counterName);
                    }
                }
                
                int origId = rs.getInt(1);
                int nextId = origId + 1;
                
                updateCounterPstmt.setInt(1, nextId);
                updateCounterPstmt.setInt(3, origId);
                
                LogService.log(LogService.DEBUG, "RDBMCounterStore::getIncrementInteger(" + counterName + ", " + nextId + ", " + origId + "): " + updateCounterQuery);
                int rowsUpdated = updateCounterPstmt.executeUpdate();
                
                if (rowsUpdated > 0) { 
                    return nextId;
                }
                else {
                    // Assume concurrent update (from other server). Try again after some random amount of milliseconds. 
                    Thread.sleep(java.lang.Math.round(java.lang.Math.random()* 3 * 1000)); // Retry in up to 3 seconds
                }
            }          // end try 
        } 
        catch (SQLException sqle) {
            PortalException pe = new PortalException("RDBMCounterStore::getIncrementInteger(): An error occured while updating the counter, name: " + counterName, sqle);
            LogService.log(LogService.ERROR, pe);
            throw pe;
        } 
        finally {
            try { getCounterPstmt.close(); } catch (Exception e) {}
            try { updateCounterPstmt.close(); } catch (Exception e) {}
            try { rs.close(); } catch (Exception e) {}
            
            RDBMServices.releaseConnection(con);
        }
        
        PortalException pe = new PortalException("RDBMCounterStore::getIncrementInteger(): Unable to increment counter for " + counterName);
        LogService.log(LogService.ERROR, pe);
        throw pe;
    }

}
