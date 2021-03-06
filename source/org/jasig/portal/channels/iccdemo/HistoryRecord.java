package org.jasig.portal.channels.iccdemo;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A trivial class for keeping a queue of N strings.
 * This class is bound to jndi "chan-obj" context by CHistory, so
 * that CViewer (or other channels) could add to the history list.
 *
 * @author <a href="mailto:pkharchenko@interactivebusiness.com">Peter Kharchenko</a>
 * @version $Revision$
 */
public class HistoryRecord {
    LinkedList history=new LinkedList();
    int maxRecords=10;

    public HistoryRecord() {};
    public HistoryRecord(int maxRecords) {
        this.maxRecords=maxRecords;
    }

    public void addHistoryRecord(String newRecord) {
        history.addFirst(newRecord);
        for(int i=0;i<history.size()-maxRecords;i++) {
            history.removeLast();
        }
    }

    public Iterator constIterator() {
        return (Collections.unmodifiableList(history)).iterator();
    }

    public String get(int i) {
        return (String)history.get(i);
    }
}
