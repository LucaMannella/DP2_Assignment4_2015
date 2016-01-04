package it.polito.dp2.WF.lab4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.dp2.WF.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class WFTests {
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    
    class ActionReaderComparator implements Comparator<ActionReader> {
        public int compare(ActionReader f0, ActionReader f1) {
        	return f0.getName().compareTo(f1.getName());
        }
    }
    
    class WorkflowReaderComparator implements Comparator<WorkflowReader> {
        public int compare(WorkflowReader f0, WorkflowReader f1) {
        	return f0.getName().compareTo(f1.getName());
        }
    }
    
    class ProcessReaderComparator implements Comparator<ProcessReader> {
        public int compare(ProcessReader f0, ProcessReader f1) {
        	return f0.getStartTime().compareTo(f1.getStartTime());
        }
    }

    
	private static WorkflowMonitor referenceWorkflowMonitor;	// reference data generator
	private static WorkflowMonitor testWorkflowMonitor;			// implementation under test
	private static long testcase;
	private static long numproc;
	
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	// Create reference data generator
        System.setProperty("it.polito.dp2.WF.WorkflowMonitorFactory", "it.polito.dp2.WF.Random.WorkflowMonitorFactoryImpl");

        referenceWorkflowMonitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();

        // Create implementation under test
        System.setProperty("it.polito.dp2.WF.WorkflowMonitorFactory", "it.polito.dp2.WF.sol4.client1.WorkflowMonitorFactory");

        testWorkflowMonitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();
        
        // read testcase property
        Long testcaseObj = Long.getLong("it.polito.dp2.WF.Random.testcase");
        if (testcaseObj == null)
        	testcase = 0;
        else
        	testcase = testcaseObj.longValue();
        
        // read number of processes created before starting the test
        Long numprocObj = Long.getLong("it.polito.dp2.WF.lab4.test.numproc");
        if (numprocObj == null)
        	numproc = 0;
        else
        	numproc = numprocObj.longValue();
        
    }
    
    @Before
    public void setUp() throws Exception {
        assertNotNull("Internal tester error during test setup: null reference", referenceWorkflowMonitor);
        assertNotNull("Could not run tests: the implementation under test generated a null WorkflowMonitor", testWorkflowMonitor);
    }

	// method for comparing two non-null strings    
	private void compareString(String rs, String ts, String meaning) {
		assertNotNull("NULL "+meaning, ts);
        assertEquals("Wrong "+meaning, rs, ts);		
	}
	
    @Test
    public final void testGetWorkflows() {
    		// call getWorkflows on the two implementations
			Set<WorkflowReader> rs = referenceWorkflowMonitor.getWorkflows();
			Set<WorkflowReader> ts = testWorkflowMonitor.getWorkflows();
			
			// if one of the two calls returned null while the other didn't return null, the test fails
	        if ((rs == null) && (ts != null) || (rs != null) && (ts == null)) {
	            fail("getWorkflows returns null when it should return non-null or vice versa");
	            return;
	        }

	        // if both calls returned null, there are no workflows, and the test passes
	        if ((rs == null) && (ts == null)) {
	            assertTrue("There are no Workflows!", true);
	            return;
	        }
	        
	        // check that the number of workflows matches
	        assertEquals("Wrong Number of Workflows", rs.size(), ts.size());
	        
	        // create treesets of workflows, using the comparator for sorting, one for reference and one for impl. under test 
	        TreeSet<WorkflowReader> rts = new TreeSet<WorkflowReader>(new WorkflowReaderComparator());
	        TreeSet<WorkflowReader> tts = new TreeSet<WorkflowReader>(new WorkflowReaderComparator());
	    
	        rts.addAll(rs);
	        tts.addAll(ts);
	        
	        // check that all workflows match one by one
	        Iterator<WorkflowReader> ri = rts.iterator();
	        Iterator<WorkflowReader> ti = tts.iterator();

	        while (ri.hasNext() && ti.hasNext()) {
	        	compareWorkflowReader(ri.next(),ti.next());
	        }
    }

    // private method for comparing two non-null WorkflowReader objects
	private void compareWorkflowReader(WorkflowReader rwr, WorkflowReader twr) {
		// check the WorkflowReaders are not null
		assertNotNull("Internal tester error: null workflow reader", rwr);
        assertNotNull("Unexpected null workflow reader", twr);
        System.out.println("Comparing workflow "+rwr.getName());

        // check the WorkflowReaders return the same data
        compareString(rwr.getName(), twr.getName(), "workflow name");
	}
	
    @Test
    public final void testGetProcesses() {
    	// call getWorkflows on the two implementations
    	Set<ProcessReader> rs = referenceWorkflowMonitor.getProcesses();
		Set<ProcessReader> ts = testWorkflowMonitor.getProcesses();
		
		// check the resulting process sets are equal
	    compareProcessSets(rs, ts);
    }

    // private method for comparing two non-null ProcessReader sets
	private void compareProcessSets(Set<ProcessReader> rs, Set<ProcessReader> ts) {
		// if one of the two calls returned null while the other didn't return null, the test fails
		if ((rs == null) && (ts != null) || (rs != null) && (ts == null)) {
		    fail("getProcesses returns null when it should return non-null or vice versa");
		    return;
		}

        // if both calls returned null, there are no processes, and the test passes
		if ((rs == null) && (ts == null)) {
		    assertTrue("There are no processes!", true);
		    return;
		}
		
        // check that the number of processes matches
		assertEquals("Wrong Number of processes", rs.size()+numproc, ts.size());
		
        // create treesets of processes, using the comparator for sorting, one for reference and one for impl. under test 
		TreeSet<ProcessReader> rts = new TreeSet<ProcessReader>(new ProcessReaderComparator());
		TreeSet<ProcessReader> tts = new TreeSet<ProcessReader>(new ProcessReaderComparator());
   
		// add all reference processes to reference process set
		rts.addAll(rs);
		// add numproc ArticleProduction processes to reference process set
		WorkflowReader wfr = referenceWorkflowMonitor.getWorkflow("ArticleProduction");
		for (int i=0; i<numproc; i++)
			rts.add(new ProcessReaderImpl(wfr));
		// add all test processes to test process set
		tts.addAll(ts);
		
		Iterator<ProcessReader> ri = rts.iterator();
		Iterator<ProcessReader> ti = tts.iterator();

        // check that all processes match one by one
		while (ri.hasNext() && ti.hasNext()) {
			compareProcessReader(ri.next(),ti.next());
		}
	}


    // private method for comparing two non-null ProcessReader objects
	private void compareProcessReader(ProcessReader rpr, ProcessReader tpr) {
		assertNotNull("Internal tester error: null process reader", rpr);
        assertNotNull("A null ProcessReader has been found", tpr);
        
        System.out.println("Comparing process workflow " + rpr.getWorkflow().getName() + " start time " + dateFormat.format(rpr.getStartTime().getTime()));
        
        // Test start time
        /* start time is not required to match
        if (testcase==2) {
	        Calendar rc = rpr.getStartTime();
	        Calendar tc = tpr.getStartTime();
			compareTime(rc, tc, "start time");
        }
        */
        
        // Test workflow
        WorkflowReader rwr = rpr.getWorkflow();
        WorkflowReader twr = tpr.getWorkflow();
		assertNotNull("Internal tester error: null workflow reader", rwr);
        assertNotNull("A null workflow reader has been found", twr);
        assertEquals("Wrong Workflow in process",rwr.getName(),twr.getName());
        
        // Test status
        List<ActionStatusReader> rstat = rpr.getStatus();
        List<ActionStatusReader> tstat = tpr.getStatus();
        if (rstat!=null) {
        	assertNotNull("A null action status list has been found", tstat);
        	assertEquals("Wrong number of action status elements", rstat.size(), tstat.size());
        	if (rstat.size()>0)
        		compareActionStatus(rstat.get(0), tstat.get(0));
        }   
	}

	private void compareTime(Calendar rc, Calendar tc, String meaning) {
		assertNotNull(rc);
		assertNotNull("Null "+meaning, tc);
		
		// Compute lower and upper bounds for checking with precision of 1 minute
		Calendar upperBound, lowerBound;
		upperBound = (Calendar)rc.clone();
		upperBound.add(Calendar.MINUTE, 1);
		lowerBound = (Calendar)rc.clone();
		lowerBound.add(Calendar.MINUTE, -1);
		
		// Compute the condition to be checked
		boolean condition = tc.after(lowerBound) && tc.before(upperBound);
		
		assertTrue("Wrong "+meaning, condition);
	}

	private void compareActionStatus(ActionStatusReader rasr, ActionStatusReader tasr) {

    	assertNotNull("Internal tester error: null action status reader", rasr);
    	assertNotNull("A null action status reader has been found", tasr);

    	System.out.println("Comparing action "+rasr.getActionName());
		compareString(rasr.getActionName(), tasr.getActionName(), "action name");
		assertTrue("Wrong taken over condition in action "+rasr.getActionName(), rasr.isTakenInCharge()==tasr.isTakenInCharge());
		assertTrue("Wrong terminated condition in action "+rasr.getActionName(), rasr.isTerminated()==tasr.isTerminated());
		
		if(rasr.isTakenInCharge()) {
			Actor ta = tasr.getActor();
			assertNotNull("Null actor in taken action", ta);
			compareString(rasr.getActor().getName(), ta.getName(), "actor name");
			compareString(rasr.getActor().getRole(), ta.getRole(), "actor role");
		}
		
		if(rasr.isTerminated()) {
			Calendar ttt = tasr.getTerminationTime();
			assertNotNull("Null termination time in terminated action", ttt);
	        if (testcase==2) {
				compareTime(rasr.getTerminationTime(), ttt, "termination time");
	        }
		}
	}
}
