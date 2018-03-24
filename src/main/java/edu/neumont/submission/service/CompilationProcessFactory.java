package edu.neumont.submission.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.neumont.submission.model.Language;

public class CompilationProcessFactory {
	private AtomicInteger numberOfRunningProcesses = new AtomicInteger(0);
	private List<Process> processes = new ArrayList<Process>();
	
	private String[] getExecutionCommand(Language language) {
		if ( language == Language.JAVA ) {
			String javaHome = System.getProperty("java.home");
			File file = new File(javaHome);
			File java = new File(new File(file.getParent(), "bin"), "java");
			
			return new String[] { java.getAbsolutePath(), "-Djava.security.manager", "Solution" };
		} else if ( language == Language.CSHARP ) {
			return new String[] { "mono", "--security=core-clr", "Solution.exe" };
		} else {
			return null;
		}
	}
	
	public Process getProcessForCompilation(Language language, File parent, File errout) {
		ProcessBuilder pb = new ProcessBuilder().command(getExecutionCommand(language)).directory(parent).redirectError(errout);
		return new Process() {
			private Process p;
			
			private synchronized Process get() {
				if ( p == null ) {
					while ( numberOfRunningProcesses.get() >= 10 ) {
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					numberOfRunningProcesses.incrementAndGet();
				} 
				
				try {
					return p = pb.start();
				} catch ( IOException e ) {
					throw new RuntimeException(e);
				}
			}
			
			private synchronized void set() {
				numberOfRunningProcesses.decrementAndGet();
				notify();
			}
			
			@Override
			public OutputStream getOutputStream() {
				return get().getOutputStream();
			}

			@Override
			public InputStream getInputStream() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getErrorStream() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int waitFor() throws InterruptedException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int exitValue() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void destroy() {
				set();
			}
			
		};
	}
}
