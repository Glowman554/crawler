package gq.glowman554.crawler.utils;

public class ThreadHelper
{
	private Thread[] threads;

	public ThreadHelper(int num_threads, ThreadHelperLambda l)
	{
		threads = new Thread[num_threads];

		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new Thread(l::run);
		}
	}

	public ThreadHelper start()
	{
		for (int i = 0; i < threads.length; i++)
		{
			threads[i].start();
		}

		return this;
	}

	public ThreadHelper join()
	{
		for (int i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		return this;
	}

	public static interface ThreadHelperLambda
	{
		void run();
	}
}
