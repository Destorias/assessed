import java.util.concurrent.*;

class fileCrawler {

	void main(String[] args) {
		
		DirectoryTree dTree = new DirectoryTree();	
		
		// Print an error message if we don't have a valid input.
		if(args.length == 0) { 
			System.err.println("Usage: 'java -classpath fileCrawler pattern [directory]'"); 	
			
		// We have valid input! Let's do our thing. 	
		} else {

			int crawler_threads_in = Integer.parseInt( System.getenv("CRAWLER_THREADS") );
			
			// Find the pattern, and determine whether it's a word or a BASH pattern.
			String pattern = args[0];
			String regex = Regex.cvtPattern(pattern);
			
			// Populate a work queue.
			ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();

			for (int index = 1; index < args.length; index++) {
			//	
			}

		} 	
	}

}
