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
import javax.transaction.Transactional;

//Export exec times to CSV
import com.opencsv.CSVWriter;

public class Main extends Db {
	
	public static void versionExpCsv() throws IOException{
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
	    
		ArrayList<Double> timesExp1 = new ArrayList<Double>();
		
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
		
	    endTime = System.nanoTime();
	    duration = (endTime - startTime);
	    //duration = expPasso1(utilDb, pok1, transacCount);
	    System.out.println("Duration: "+duration);
	    
	    
	    //CSV things
	    //Um CSV para cada exp
	    //System.out.println("Times:" + times);
	    String[] header = {"1000", "10000", "100000"};
	    

	    
	    //Executar os experimentos para tirar a media depois nVezes
	    int nVezes = 10;
	    //Exp1
	    //1000
	    String expN = "exp1";
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+expN+".csv";
	    String[] headerAux = {"1000", "10000", "100000"};
	    listDataSet.add(headerAux);
	    
	    //utilDb.saveToCsv(listDataSet, csvPath, true);
	    
	    //Dropar os registros antes de popular
	    //Depois fazer os mesmos testes com a base grande
	    
	    //Também fazer a versão com '1000.csv'
	    //Exp1, Exp2, Exp3, Exp4
	    //Para comparar as versões entre si 
	    
	    //Exp1
	    transacCount = 1000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp1(transacCount, utilDb, pok, pok1));
	    transacCount = 10000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp1(transacCount, utilDb, pok, pok1));
	    transacCount = 100000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp1(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    String[] timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(timesRecord1);
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
	    
	    
	    
	    
	    
	    
	    
	    
	    timesExp1.removeAll(timesExp1);
	    //Exp2
	    expN = "exp2";
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+expN+".csv";
	    
	    transacCount = 1000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp2(transacCount, utilDb, pok, pok1));
	    transacCount = 10000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp2(transacCount, utilDb, pok, pok1));
	    transacCount = 100000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp2(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(timesRecord1);
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
	    
	    
	    
	    
	    
	    
	    timesExp1.removeAll(timesExp1);
	    //Exp3
	    expN = "exp3";
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+expN+".csv";
	    
	    transacCount = 1000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp3(transacCount, utilDb, pok, pok1));
	    transacCount = 10000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp3(transacCount, utilDb, pok, pok1));
	    transacCount = 100000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp3(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(timesRecord1);
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
	    
	    
	    
	    
	    timesExp1.removeAll(timesExp1);
	    //Exp4
	    expN = "exp4";
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+expN+".csv";
	    
	    transacCount = 1000;
	    
	    //utilDb.populate(em, pok, transacCount/2);
	    
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp4(transacCount, utilDb, pok, pok1));
	    transacCount = 10000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp4(transacCount, utilDb, pok, pok1));
	    transacCount = 100000;
	    System.out.println("Transações: " + transacCount);
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp4(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(timesRecord1);
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
		
		
	}
	//@Transactional
//	public static void executeDropTable(String tableName, EntityManager em){
//		
//		System.out.println("Dropando: " + tableName);
//	    String query = "DROP TABLE pokemon";
//		//String query = " DELETE FROM `pokemon` WHERE `pokemon`.`id` = 1;";
//	    em.getTransaction().begin();
//	    em.createNativeQuery(query).executeUpdate();
//	    
//	    em.getTransaction().commit();
//	}
	
	public static void versionTransCountCsv() throws IOException, InterruptedException {
		ArrayList<Integer> listaInt = new ArrayList<Integer>();
		MyCounter counter = new MyCounter();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		EntityManager em = emf.createEntityManager();
		
		UtilDb utilDb = new UtilDb(emf);
		
		final Pokemon pok = new Pokemon(null, "Ivysaur");
		final Pokemon pok1 = new Pokemon(null, "Venusaur");
		
		//Na verdade 1000 transações são 500 + 500 e não 1 até 499 e 500 até 1000
		//Então só precisa de 500 para 100 transações por exemplo
		
		
		long startTime = System.nanoTime();
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
	    String csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\test.csv";

		int Threads = 2;
	    
		ArrayList<Double> timesExp1 = new ArrayList<Double>();
		
		ArrayList<Long> timesExp2 = new ArrayList<Long>();
		
		ArrayList<Long> timesExp3 = new ArrayList<Long>();
		
		ArrayList<Long> timesExp4 = new ArrayList<Long>();
		
		ArrayList<Long> times = new ArrayList<Long>();
		
		ArrayList<String> timeS = new ArrayList<String>();
		
		List<String[]> listDataSet = new ArrayList<>();
		
		
		Integer transacCount = 1000;
		//Popular a base
		utilDb.populate(em, pok, 1);
		
		startTime = System.nanoTime();
		
	    endTime = System.nanoTime();
	    duration = (endTime - startTime);
	    //duration = expPasso1(utilDb, pok1, transacCount);
	    System.out.println("Duration: "+duration);
	    
	    
	    //CSV things
	    //Um CSV para cada exp
	    //System.out.println("Times:" + times);
	    String[] header = {"1000", "10000", "100000"};
	    

	    
	    //Executar os experimentos para tirar a media depois nVezes
	    int nVezes = 10;
	    //Exp1
	    //1000
	    String expN = "1000";
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+expN+".csv";
	    String[] headerAux = {"Exp1", "Exp2", "Exp3", "Exp4"};
	    listDataSet.add(headerAux);
	    
	    //utilDb.saveToCsv(listDataSet, csvPath, true);
	    
	    //Dropar os registros antes de popular
	    //Depois fazer os mesmos testes com a base grande
	    
	    //Também fazer a versão com '1000.csv'
	    //Exp1, Exp2, Exp3, Exp4
	    //Para comparar as versões entre si 
	    
	    //1000
	    //executeDropTable("pokemon",em);
	    //executeDropTable("pokemon", em);
	    transacCount = 1000;
	    
	    
	    
	    System.out.println("Transações: " + transacCount);
	    Db.executeDropTable("pokemon");
	    utilDb.populate(em, pok, transacCount/2);
	    
	    System.out.println("Exp1:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    timesExp1.add(avgExp1(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);
	    
	    System.out.println("Exp2:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp2(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    
	    System.out.println("Exp3:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp3(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    System.out.println("Exp4:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp4(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    
	    String[] timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(timesRecord1);
	    
	    
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+transacCount+".csv";
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
	    
	    
	    
	    
	    
	    
	    
	    listDataSet.removeAll(listDataSet);
	    timesExp1.removeAll(timesExp1);
	    //10000 Transações
	    transacCount = 10000;
	    
	    System.out.println("Transações: " + transacCount);
	    Db.executeDropTable("pokemon");
	    utilDb.populate(em, pok, transacCount/2);
	    
	    System.out.println("Exp1:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    timesExp1.add(avgExp1(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);
	    
	    System.out.println("Exp2:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp2(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    
	    System.out.println("Exp3:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp3(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    System.out.println("Exp4:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp4(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    
	    timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(headerAux);
	    listDataSet.add(timesRecord1);
	    
	    
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+transacCount+".csv";
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
	    
	    listDataSet.removeAll(listDataSet);
	    timesExp1.removeAll(timesExp1);
	    //100000 Transações
	    transacCount = 100000;
	    
	    
	    
	    System.out.println("Transações: " + transacCount);
	    Db.executeDropTable("pokemon");
	    utilDb.populate(em, pok, transacCount/2);
	    
	    System.out.println("Exp1:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    timesExp1.add(avgExp1(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);
	    
	    System.out.println("Exp2:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp2(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    
	    System.out.println("Exp3:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp3(transacCount, utilDb, pok, pok1));
	    //TimeUnit.Seconds.sleep(15);

	    System.out.println("Exp4:");
	    //System.out.println("Avg:"+avgExp1(transacCount, utilDb, em, pok, pok1));
	    timesExp1.add(avgExp4(transacCount, utilDb, pok, pok1));
	    
	    
	    
	    
	    timesRecord1 = new String[timesExp1.size()];

	    for(int i = 0; i < timesExp1.size(); i++) {
	    	timesRecord1[i] = timesExp1.get(i).toString();
	    }
	    
	    listDataSet.add(headerAux);
	    listDataSet.add(timesRecord1);
	    
	    
	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+transacCount+".csv";
	    
	    utilDb.saveToCsv(listDataSet, csvPath, true);
	    System.out.println("ListaTimes: " + timesExp1);
	    
//	    timesExp1.removeAll(timesExp1);
//	    //10000
//	    transacCount = 10000;
//	    System.out.println("Transações: " + transacCount);
//	    
//	    System.out.println("Exp1:");
//	    timesExp1.add(avgExp1(transacCount, utilDb, em, pok, pok1));	    
//	    
//	    System.out.println("Exp2:");
//	    timesExp1.add(avgExp2(transacCount, utilDb, em, pok, pok1));
//	    
//	    
//	    System.out.println("Exp3:");
//	    timesExp1.add(avgExp3(transacCount, utilDb, em, pok, pok1));
//	    
//	    System.out.println("Exp4:");
//	    timesExp1.add(avgExp4(transacCount, utilDb, em, pok, pok1));
//	    
//	    
//	    
//	    
//	    timesRecord1 = new String[timesExp1.size()];
//
//	    for(int i = 0; i < timesExp1.size(); i++) {
//	    	timesRecord1[i] = timesExp1.get(i).toString();
//	    }
//	    
//	    listDataSet.add(timesRecord1);
//	    
//	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+transacCount+".csv";
//
//	    utilDb.saveToCsv(listDataSet, csvPath, true);
//	    System.out.println("ListaTimes: " + timesExp1);	    
//	    
//	    
//	    
//	    
//	    
//	    timesExp1.removeAll(timesExp1);
//	    //100000
//	    transacCount = 100000;
//	    System.out.println("Transações: " + transacCount);
//	    
//	    System.out.println("Exp1:");
//	    timesExp1.add(avgExp1(transacCount, utilDb, em, pok, pok1));
//	    
//	    
//	    System.out.println("Exp2:");
//	    timesExp1.add(avgExp2(transacCount, utilDb, em, pok, pok1));
//	    
//	    
//	    System.out.println("Exp3:");
//	    timesExp1.add(avgExp3(transacCount, utilDb, em, pok, pok1));
//	    
//	    System.out.println("Exp4:");
//	    timesExp1.add(avgExp4(transacCount, utilDb, em, pok, pok1));
//	    
//	    
//	    
//	    
//	    timesRecord1 = new String[timesExp1.size()];
//
//	    for(int i = 0; i < timesExp1.size(); i++) {
//	    	timesRecord1[i] = timesExp1.get(i).toString();
//	    }
//	    
//	    listDataSet.add(timesRecord1);
//	    
//	    
//	    csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\"+transacCount+".csv";
//
//	    utilDb.saveToCsv(listDataSet, csvPath, true);
//	    System.out.println("ListaTimes: " + timesExp1);
//	    
//	    
//	    timesExp1.removeAll(timesExp1);
//	    
//	    
		
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		
		//versionExpCsv();
		
		versionTransCountCsv(); 
		//executeDropTable();
	    
//	    String[] timesRecord = new String[timesExp1.size()];
//	    
//	    for(int i = 0; i < timesExp1.size(); i++) {
//	    	timesRecord[i] = timesExp1.get(i).toString();
//	    }
	    
	    
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
	    //timeS.add(avg.toString());
    	//listDataSet.add();
    	//utilDb.saveToCsv(listDataSet, csvPath, true);
	    
	    //timesExp1.add(expPasso1(utilDb, pok1, transacCount));
	    
	    //TimeUnit.SECONDS.sleep(60);
	    
	   
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
