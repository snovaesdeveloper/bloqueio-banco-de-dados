package app;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
//Talvez randomizar os i's dos for's para buscas e atualizações
import javax.persistence.Query;

//Export exec times to CSV
import com.opencsv.CSVWriter;

public class Db {
	public static long expPasso1(UtilDb utilDb, Pokemon pok1, Integer transacCount) throws InterruptedException, ExecutionException {
		int Threads = 2;
		long duration = 0;
	    ExecutorService threadpool = Executors.newCachedThreadPool();
	    //threadpool.awaitTermination(1, TimeUnit.SECONDS);
	    Future<?> futureTask = threadpool.submit(() -> System.out.println("HI"));
	    long startTime = System.nanoTime();
		for (int i = 1; i<Threads; i++) {
	    	//futureTask = threadpool.submit(() ->  utilDb.update(em, pok1, 10));
	    	//Primeiro Nivel(100% das trans pedem shared lock)
	    	//Consulta
	    	futureTask = threadpool.submit(() ->  utilDb.buscaShared(pok1, transacCount/2));
	    	//Escrita
	    	futureTask = threadpool.submit(() ->  utilDb.updateShared(pok1, transacCount/2));
	    }
		while (!futureTask.isDone()) {
	    	//System.out.println("ExpPasso1 is not finished yet..."); 
	    }
		if(futureTask.isDone()) {
			System.out.println("ExpPasso1 is finished!"); 
			long endTime = System.nanoTime();

			duration = (endTime - startTime);
			return duration;
			
		}
	

	    System.out.println(futureTask.get());    
	    threadpool.shutdown();
	    return duration;
	}
	
	public static long expPasso2(UtilDb utilDb, Pokemon pok1, Integer transacCount) throws InterruptedException, ExecutionException {
		int Threads = 2;
		long duration = 0;
	    ExecutorService threadpool = Executors.newCachedThreadPool();
	    //threadpool.awaitTermination(1, TimeUnit.SECONDS);
	    Future<?> futureTask = threadpool.submit(() -> System.out.println("HI"));
	    long startTime = System.nanoTime();
	    for (int i = 1; i<Threads; i++) {
	    	
	    	//Segundo Nivel(Só trans de leitura pedem shared lock)
	    	//Consulta
	    	futureTask = threadpool.submit(() ->  utilDb.buscaShared(pok1, transacCount/2));
	    	//Escrita
	    	futureTask = threadpool.submit(() ->  utilDb.update(pok1, transacCount/2));
	
	    }
		while (!futureTask.isDone()) {
	    	//System.out.println("ExpPasso2 is not finished yet..."); 
	    }
		if(futureTask.isDone()) {
			System.out.println("ExpPasso2 is finished!"); 
			long endTime = System.nanoTime();

			duration = (endTime - startTime);
			return duration;
			
		}
		System.out.println(futureTask.get());    
	    threadpool.shutdown();
		return duration;
	    
	}
	
	public static long expPasso3(UtilDb utilDb, Pokemon pok1, Integer transacCount) throws InterruptedException, ExecutionException {
		int Threads = 2;
		long duration = 0;
	    ExecutorService threadpool = Executors.newCachedThreadPool();
	    //threadpool.awaitTermination(1, TimeUnit.SECONDS);
	    Future<?> futureTask = threadpool.submit(() -> System.out.println("HI"));
	    long startTime = System.nanoTime();
	    for (int i = 1; i<Threads; i++) {
	    	
	    	//Terceiro Nivel(Só trans de escrita pedem exclusive lock)
	    	//Consulta
	    	futureTask = threadpool.submit(() ->  utilDb.busca(pok1, transacCount/2));
	    	//Escrita
	    	futureTask = threadpool.submit(() ->  utilDb.updateExclusive(pok1, transacCount/2));
	    	
	   
	    }
	    while (!futureTask.isDone()) {
	    	//System.out.println("ExpPasso3 is not finished yet..."); 
	    }
		if(futureTask.isDone()) {
			System.out.println("ExpPasso3 is finished!"); 
			long endTime = System.nanoTime();

			duration = (endTime - startTime);
			return duration;
			
		}
	    System.out.println(futureTask.get());    
	    threadpool.shutdown();
	    return duration;
	}
	
	public static long expPasso4(UtilDb utilDb, Pokemon pok1, Integer transacCount) throws InterruptedException, ExecutionException {
		int Threads = 2;
		long duration = 0;
	    ExecutorService threadpool = Executors.newCachedThreadPool();
	    //threadpool.awaitTermination(1, TimeUnit.SECONDS);
	    Future<?> futureTask = threadpool.submit(() -> System.out.println("HI"));
	    long startTime = System.nanoTime();
	    for (int i = 1; i<Threads; i++) {
	    	
	    	//Quarto Nivel(Só todas trans de escrita pedem exclusive lock 
	    	//e todas as trans de leitura pedem shared lock)
	    	//Consulta
	    	futureTask = threadpool.submit(() ->  utilDb.buscaShared(pok1, transacCount));
	    	//Escrita
	    	futureTask = threadpool.submit(() ->  utilDb.updateExclusive(pok1, transacCount));
	    	
	    }
	    while (!futureTask.isDone()) {
	    	//System.out.println("ExpPasso4 is not finished yet..."); 
	    }
		if(futureTask.isDone()) {
			System.out.println("ExpPasso4 is finished!"); 
			long endTime = System.nanoTime();

			duration = (endTime - startTime);
			return duration;
			
		}
	    System.out.println(futureTask.get());    
	    threadpool.shutdown();
	    return duration;
		
	}

	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		ArrayList<Integer> listaInt = new ArrayList<Integer>();
		MyCounter counter = new MyCounter();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		EntityManager em = emf.createEntityManager();
		
		UtilDb utilDb = new UtilDb();
		
		final Pokemon pok = new Pokemon(null, "Ivysaur");
		final Pokemon pok1 = new Pokemon(null, "Venusaur");
		
		//Na verdade 1000 transações são 500 + 500 e não 1 até 499 e 500 até 1000
		//Então só precisa de 500 para 100 transações por exemplo
		
		
		long startTime = System.nanoTime();
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
	    String csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\test.csv";

		int Threads = 2;
	    
		ArrayList<Long> timesExp1 = new ArrayList<Long>();
		
		ArrayList<Long> timesExp2 = new ArrayList<Long>();
		
		ArrayList<Long> timesExp3 = new ArrayList<Long>();
		
		ArrayList<Long> timesExp4 = new ArrayList<Long>();
		
		ArrayList<Long> times = new ArrayList<Long>();
		
		ArrayList<String> timeS = new ArrayList<String>();
		
		List<String[]> listDataSet = new ArrayList<>();
		
		
		Integer transacCount = 1000;
		//Popular a base
		utilDb.populate(em, pok, transacCount/2);
		
		startTime = System.nanoTime();
		
	    //times.add(expPasso1(utilDb, pok1, transacCount));
	    //times.add(expPasso2(utilDb, pok1, transacCount));
	    //times.add(expPasso3(utilDb, pok1, transacCount));
	    //times.add(expPasso4(utilDb, pok1, transacCount));
	    
	    
	    endTime = System.nanoTime();
	    duration = (endTime - startTime);
	    //duration = expPasso1(utilDb, pok1, transacCount);
	    System.out.println("Duration: "+duration);
	    
	    
	    //CSV things
	    //Um CSV para cada exp
	    //System.out.println("Times:" + times);
	    String[] header = {"1000", "10000", "100000"};
	    
	    //String[] header = {"expPasso1", "expPasso2", "expPasso3", "expPasso4"};
	    
//	    listDataSet.add(header);
//	    //Long[] timesRecord = {}
//	    String[] timesRecord = new String[times.size()];
//	    
//	    
//	    
//	    
//	    //listDataSet.add(header);
//	    
//	    
//	    for(int i = 0; i < times.size(); i++) {
//	    	timesRecord[i] = times.get(i).toString();
//	    }
//	    
//	    listDataSet.add(timesRecord);
//	    
//	    utilDb.saveToCsv(listDataSet, csvPath);
	    
	    
	    
	    //Executar os experimentos para tirar a media depois nVezes
	    int nVezes = 10;
	    //Exp1
	    //1000
	    String expN = "exp1";
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+expN+".csv";
	    String[] headerAux = {"1000", "10000", "100000"};
	    listDataSet.add(headerAux);
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    
	    transacCount = 1000;
	    utilDb.populate(em, pok, transacCount/2);
	    TimeUnit.SECONDS.sleep(15);
	    for(int i= 0; i < 10; i++) {
	    	
	    	timesExp1.add(expPasso1(utilDb, pok1, transacCount));
	    	
	    	TimeUnit.SECONDS.sleep(5);
	    }
	    
	    String[] timesRecord = new String[times.size()];
	    
	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord[i] = timesExp1.get(i).toString();
	    }
	    //Tirar media
	    Double avg = timesExp1.stream()
        .mapToDouble(d -> d)
        .average()
        .orElse(0.0);
	    //!!!!!!!!!!!
	    //Dilema 
	    //Calcular para por exemplo 1000 o exp1, exp2, exp3 e exp4 (Como feito da primeira vez (Com medias))
	    //Sendo os arquivos 1000.csv, 10000.csv e 100000.csv.
	    //#1000.csv
	    //exp1, exp2, exp3 e exp4
	    //123  ,  500,  432, 1337
	    
	    //Ou 
	    //Para cada exp1 fazer um csv com os tempos para cada 1000, 10000, 100000.
	    //Sendo os arquivos exp1.csv, exp2.csv, exp3.csv, exp4.csv.
	    //#exp1
	    //1000, 10000, 100000
	    //123,  4324,   5433
	    
	    
	    //!!!!!!!!!!!
	    timeS.add(avg.toString());
    	//listDataSet.add();
    	utilDb.saveToCsv(listDataSet, csvPath, true);
	    
	    //timesExp1.add(expPasso1(utilDb, pok1, transacCount));
	    
	    //TimeUnit.SECONDS.sleep(60);
	    
	    //10000
	    transacCount = 10000;
	    
	    //TimeUnit.SECONDS.sleep(60);
	    //100000
	    transacCount = 100000;
	    
	    //TimeUnit.SECONDS.sleep(60);
	    
	    
	    
	    
	    
	    //Exp2
	    //1000
	    transacCount = 1000;
	    //10000
	    transacCount = 10000;
	    //100000
	    transacCount = 100000;
	    
	    //Exp3
	    transacCount = 1000;
	    //10000
	    transacCount = 10000;
	    //100000
	    transacCount = 100000;
	    
	    //Exp4
	    transacCount = 1000;
	    //10000
	    transacCount = 10000;
	    //100000
	    transacCount = 100000;
	    
//	    String[] header = {"expPasso1", "expPasso2", "expPasso3", "expPasso4"};
//	    listDataSet.add(header);
//	    //Long[] timesRecord = {}
//	    String[] timesRecord = new String[times.size()];
//	    
//	    
//	    
//	    
//	    //listDataSet.add(header);
//	    
//	    
//	    for(int i = 0; i < times.size(); i++) {
//	    	timesRecord[i] = times.get(i).toString();
//	    }
//	    
//	    listDataSet.add(timesRecord);
//	    
//	    utilDb.saveToCsv(listDataSet, csvPath);
//	    
////	    String csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\test.csv";
////	    try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
////            writer.writeAll(list);
////        }
////	    
//	    //System.out.println("TimesR:" + timesRecord);
//	    //System.out.println(counter.getLista());
//	    
//	    for (int i = 0; i<counter.getLista().size(); i++) {
//	    	System.out.println(counter.getLista().get(i).getTime());
//	    
//	    }
	}
	
	private static void log(Object... msgs) {
	      System.out.println(LocalTime.now() + " - " + Thread.currentThread().getName() +
	              " - " + Arrays.toString(msgs));
	}

}
