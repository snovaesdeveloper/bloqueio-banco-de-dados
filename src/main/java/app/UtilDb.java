package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;


//CSV
import com.opencsv.CSVWriter;


public class UtilDb {
	//EntityManager em;
	private EntityManagerFactory emf; 
	
	
	public UtilDb(){
		
	}
	public UtilDb(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	//Utils para converter para csv
	
	
	//Popula em uma transacao
	public void populate1Go(EntityManager em) {
		//em.getTransation().begin();
		
	}
	
	//Popula em varias transacoes (id nao automatico, tem q fzr uma nova base)
	public void populate(EntityManager em, Pokemon pok, int amount) {
		//Pokemon pok = new Pokemon(null, "Gengar");
		System.out.println("Populando Db com: "+ amount+"\n");
		for(int i = 0; i<amount; i++) {
			Pokemon pokAux = new Pokemon(null, pok.getName());
			
			pokAux.setName(pokAux.getName() + i);
			this.salvar(em, pokAux);
			
		}
	}
	
	//Tentar faz com id automatico
//	public void populate(EntityManager em, Pokemon pok, int amount) {
//		//Pokemon pok = new Pokemon(null, "Gengar");
//		String Name = pok.getName();
//		
//		for(int i = 0; i<amount; i++) {
//			pok.setName(Name + i);
//			this.salvar(em,pok);
//			pok.setName(Name);
//		}
//	}
	

	
	//Sem Lock
	//Atualiza em varias transacoes sem lock
//	public void update(EntityManager em, Pokemon pok, int amount) {
//		String name = pok.getName();
//		for(int i = 1; i<amount +1; i++) {
//			 
//			Pokemon pokAux = em.find(Pokemon.class, i);
//			pokAux.setName(name + i);
//			this.salvar(em, pokAux);
//			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
//			
//			//Fechar Conexão para evitar gargalo nas conexões
//			em.close();
//	       
//		}
//	}
//	public void update3(Pokemon pok, int amount) {
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
//		String name = pok.getName();
//		for(int i = 1; i<amount +1; i++) {
//			//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
//			EntityManager em = emf.createEntityManager();
//			 
//			Pokemon pokAux = em.find(Pokemon.class, i);
//			pokAux.setName(name + i);
//			this.salvar(em, pokAux);
//			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
//		}
//	}
	//sem lock
	public void update(Pokemon pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
			
			EntityManager em = this.emf.createEntityManager();
			Pokemon pokAux = em.find(Pokemon.class, i);
			String nameAux = "";
			nameAux = name + i;
			
			pokAux.setName(nameAux);

		
			em.getTransaction().begin();
			
			em.persist(pokAux);
			//System.out.println("Salvando sem lock: "+ nameAux +"\n");
			em.getTransaction().commit();
			
			em.close();
			
		});
		//emf.close();
	}
	//com lock
	public void updateExclusive(Pokemon pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
			EntityManager em = this.emf.createEntityManager();
			String nameAux = "";
			
			em.getTransaction().begin();
			Pokemon pokAux = em.find(Pokemon.class, i, LockModeType.PESSIMISTIC_WRITE);
			nameAux = name + i;

			pokAux.setName(nameAux);
			em.persist(pokAux);
			//System.out.println("Salvando Exclusivo: "+ nameAux +"\n");
			
			em.getTransaction().commit();
	       
			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
			
			//Fechar Conexão para evitar gargalo nas conexões
			em.close();
	       
		});
		
		//emf.close();
	}
	
	//Pode isso? (escrita pedir shared)
	public void updateShared(Pokemon pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
		
			EntityManager em = this.emf.createEntityManager();
		
			String nameAux = "";
			

			em.getTransaction().begin();
			Pokemon pokAux = em.find(Pokemon.class, i, LockModeType.PESSIMISTIC_READ);
			nameAux = name + i;
			//Add isso em todos
			if(pokAux != null) {
				pokAux.setName(nameAux);
				em.persist(pokAux);
				
			}
			
			//System.out.println("Salvando SHARED: "+ nameAux +"\n");
			em.getTransaction().commit();
	        
			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
			
			//Fechar Conexão para evitar gargalo nas conexões
			em.close();
	       
		});
		
		//emf.close();
	}
	//Sem lock
	public void busca(Pokemon pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{

			EntityManager em = this.emf.createEntityManager();
			

			
			em.getTransaction().begin();
			
			Pokemon pokAux = em.find(Pokemon.class, i);

			//System.out.println("Lendo sem lock: "+ pokAux.getName() +"\n");
			em.getTransaction().commit();
	        
			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
			
			//Fechar Conexão para evitar gargalo nas conexões
			em.close();
	       
		});
		//emf.close();
	}
	//Com lock
	public void buscaShared(Pokemon pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
			
			EntityManager em = this.emf.createEntityManager();
			
			//String nameAux = "";
			

			em.getTransaction().begin();
		
			Pokemon pokAux = em.find(Pokemon.class, i, LockModeType.PESSIMISTIC_READ);

			
			
			em.getTransaction().commit();
			
			
	        
			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
			
			//Fechar Conexão para evitar gargalo nas conexões
			em.close();
	       
		});
		//emf.close();
	}
	
	//Teste sem o salvar()!!funcionando paralelo!!
//	@SuppressWarnings("unchecked")
//	public void update2(EntityManager em, Pokemon pok, int amount) {
//		String name = pok.getName();
//		//String nameAux = "";
//		//ArrayList<Integer> list = new ArrayList<>();
//		
//		IntStream.range(1,amount).boxed().parallel().forEach(i->{
//			//HibernateSessio
//			Pokemon pokAux = em.find(Pokemon.class, i);
//			String nameAux = "";
//			nameAux = name + i;
//			//nameAux = "AAAAAAhhhhh";
//			pokAux.setName(nameAux);
////			if(!em.getTransaction().isActive())
////		        em.getTransaction().begin();
////			em.persist(pokAux);
////			em.getTransaction().commit();
////			em.close();
////			
//			em.getTransaction().begin();
//			
//			em.persist(pokAux);
//			em.getTransaction().commit();
//	        // flush em - save to DB
//	        //em.flush();
//			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
//			
//			System.out.println("Salvando: "+ nameAux +"\n");
//			
//		});
////		for(int i = 1; i<amount +1; i++) {
////			
////			Pokemon pokAux = em.find(Pokemon.class, i);
////			pokAux.setName(name + i);
////			em.getTransaction().begin();
////			em.persist(pokAux);
////			System.out.println("Salvando: "+ pokAux.getName()+"\n");
////			em.getTransaction().commit();
////			
////		}
//	}
	
	public void salvar2(EntityManager em, Integer i) {
		em.getTransaction().begin();
		Pokemon pokAux = em.find(Pokemon.class, i);
		em.persist(pokAux);
		em.getTransaction().commit();
	}
	
	//Salvar sem lock
	public void salvar(EntityManager em, Pokemon pok) {
		em.getTransaction().begin();
		em.persist(pok);
		em.getTransaction().commit();
	}
	
	//Busca sem lock
	public void search(EntityManager em, Pokemon pok, int amount) {
		String name = pok.getName();
		for(int i = 1; i<amount +1; i++) {
			
			Pokemon pokAux = em.find(Pokemon.class, i);
			pokAux.setName(name + i);
		}
	}
	

		
	//Com lock
	//Shared
//	public void updateSharedLock(EntityManager em, Pokemon pok, int amount) {
//		String name = pok.getName();
//		for(int i = 1; i<amount +1; i++) {
//			 
//			Pokemon pokAux = em.find(Pokemon.class, i);
//			pokAux.setName(name + i);
//			this.salvar(em, pokAux);
//		}
//	}
//	public void salvarSharedLock(EntityManager em, Pokemon pok) {
//		em.getTransaction().begin();
//		em.lock(pok, LockModeType.PESSIMISTIC_WRITE);
//		em.persist(pok);
//		em.getTransaction().commit();
//	}
		
	//Search Shared PESSIMISTIC_READ
	//Busca varios
		public void searchSharedLock(EntityManager em, Pokemon pok, int amount) {
			String name = pok.getName();
			
			for(int i = 1; i<amount +1; i++) {
				
				Pokemon pokAux = em.find(Pokemon.class, i, LockModeType.PESSIMISTIC_READ);
				//Configurar query
				//https://www.baeldung.com/jpa-pessimistic-locking#:~:text=There%20are%20two%20types%20of,to%20have%20an%20exclusive%20lock.
				pokAux.setName(name + i);
			}
		}
	
	
	//Update Exclusive PESSIMISTIC_WRITE
//	public void updateExclusiveLock(EntityManager em, Pokemon pok, int amount) {
//		String name = pok.getName();
//		for(int i = 1; i<amount +1; i++) {
//			 
//			Pokemon pokAux = em.find(Pokemon.class, i);
//			pokAux.setName(name + i);
//			this.salvarExclusiveLock(em, pokAux);
//		}
//	}
		
	public void updateExclusiveLock(EntityManager em, Pokemon pok, int amount) {
		String name = pok.getName();
		for(int i = 1; i<amount +1; i++) {
			
			em.getTransaction().begin(); 
			
			Pokemon pokAux = em.find(Pokemon.class, i, LockModeType.PESSIMISTIC_WRITE);
			
			//em.lock(pok, LockModeType.PESSIMISTIC_WRITE);
			em.persist(pok);
			
			em.getTransaction().commit();
			pokAux.setName(name + i);
			
		}
	}
	
	public void salvarExclusiveLock(EntityManager em, Pokemon pok) {
		em.getTransaction().begin();
		em.lock(pok, LockModeType.PESSIMISTIC_WRITE);
		em.persist(pok);
		em.getTransaction().commit();
	}
	
	//CSV utils
	public void saveToCsv(List<String[]> listDataSet, String csvPath, boolean append) throws IOException {
		//String csvPath = "E:\\Downloads\\U-20220828T191257Z-001\\U\\bd2\\timesCSV\\test.csv";
	    try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath, append))) {
            writer.writeAll(listDataSet);
            //writer.writeNext(listDataSet);
        }
	}
	
	
	


	

}
