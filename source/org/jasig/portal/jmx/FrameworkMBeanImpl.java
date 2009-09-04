/* Copyright 2005 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package org.jasig.portal.jmx;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jasig.portal.PortalException;
import org.jasig.portal.PortalSessionManager;
import org.jasig.portal.ProblemsTable;
import org.jasig.portal.RDBMServices;
import org.jasig.portal.utils.MovingAverage;
import org.jasig.portal.utils.MovingAverage.Sample;

/**
 * uPortal metrics to make available to JMX.
 *
 * @author George Lindholm <a href="mailto:George.Lindholm@ubc.ca">George.Lindholm@ubc.ca</a>
 * @version $Revision$ $Date$
 * @since uPortal 2.5
 */
public class FrameworkMBeanImpl implements FrameworkMBean {
  private static MovingAverage renderTimes = new MovingAverage(100);
  private static Sample lastRender = new Sample();
  private static String database;

  public FrameworkMBeanImpl() {
    database = RDBMServices.getDataSource().toString();
  }
  /**
   * Time/Data uPortal was started
   * @return Date
   */
  public Date getStartedAt() {
    return PortalSessionManager.STARTED_AT;
  }

  public long getRenderAverage() {
    return lastRender.average;
  }
  public long getRenderHighMax() {
    return lastRender.highMax;
  }
  public long getRenderLast() {
    return lastRender.lastSample;
  }
  public long getRenderMin() {
    return lastRender.min;
  }
  public long getRenderMax() {
    return lastRender.max;
  }
  public long getRenderTotalRenders() {
    return lastRender.totalSamples;
  }

  public static void setLastRender(final long time) {
    lastRender = renderTimes.add(time);
  }

  public String[] getRecentProblems() {
    final List rpe = ProblemsTable.getRecentPortalExceptions();
    final ArrayList al = new ArrayList(rpe.size());
    for (Iterator it = rpe.iterator(); it.hasNext(); ) {
      final PortalException pe = (PortalException) it.next();
      al.add(pe.getMessage());
    }
    return (String[]) al.toArray(new String[0]);
  }

  /* Database */
  public int getRDBMActiveConnectionCount() {
    return RDBMServices.getActiveConnectionCount();

  }
  public String getRDBMDatabase() {
    return database;
  }

}