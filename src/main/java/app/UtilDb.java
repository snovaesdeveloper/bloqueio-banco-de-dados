package app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;


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
	public void populate(EntityManager em, Pessoa pok, int amount) {
		//Pokemon pok = new Pokemon(null, "Gengar");
		System.out.println("Populando Db com: "+ amount+"\n");
		for(int i = 0; i<amount; i++) {
			Pessoa pokAux = new Pessoa(null, pok.getName());
			
			pokAux.setName(pokAux.getName() + i);
			this.salvar(em, pokAux);
			
		}
	}

	//sem lock
	public void update(Pessoa pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
			
			EntityManager em = this.emf.createEntityManager();
			

		
			em.getTransaction().begin();
			Pessoa pokAux = em.find(Pessoa.class, i);
			String nameAux = "";
			nameAux = name + i;
			
			pokAux.setName(nameAux);
			
			em.persist(pokAux);
			//System.out.println("Salvando sem lock: "+ nameAux +"\n");
			em.getTransaction().commit();
			
			em.close();
			
		});
		//emf.close();
	}
	//com lock
	public void updateExclusive(Pessoa pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
			EntityManager em = this.emf.createEntityManager();
			String nameAux = "";
			
			em.getTransaction().begin();
			Pessoa pokAux = em.find(Pessoa.class, i, LockModeType.PESSIMISTIC_WRITE);
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
	public void updateShared(Pessoa pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
		
			EntityManager em = this.emf.createEntityManager();
		
			String nameAux = "";
			

			em.getTransaction().begin();
			Pessoa pokAux = em.find(Pessoa.class, i, LockModeType.PESSIMISTIC_READ);
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
	public void busca(Pessoa pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");		
		
		IntStream.range(1,amount).boxed().parallel().forEach(i->{

			EntityManager em = this.emf.createEntityManager();
			

			
			em.getTransaction().begin();
			
			Pessoa pokAux = em.find(Pessoa.class, i);

			//System.out.println("Lendo sem lock: "+ pokAux.getName() +"\n");
			em.getTransaction().commit();
	        
			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
			
			//Fechar Conexão para evitar gargalo nas conexões
			em.close();
	       
		});
		//emf.close();
	}
	//Com lock
	public void buscaShared(Pessoa pok, int amount) {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		String name = pok.getName();
		
		System.out.println("Lendo Compartilhado:?????? \n");
		IntStream.range(1,amount).boxed().parallel().forEach(i->{
			//System.out.println("Lendo Compartilhado:?????? \n");
			EntityManager em = this.emf.createEntityManager();
			
			//String nameAux = "";
			

			em.getTransaction().begin();
		
			Pessoa pokAux = em.find(Pessoa.class, i, LockModeType.PESSIMISTIC_READ);

			//System.out.println("Lendo Compartilhado: "+ pokAux.getName() +"\n");
			
			em.getTransaction().commit();
			
			
	        
			//System.out.println("Salvando: "+ pokAux.getName()+"\n");
			
			//Fechar Conexão para evitar gargalo nas conexões
			em.close();
	       
		});
		//emf.close();
	}
	

	
	public void salvar2(EntityManager em, Integer i) {
		em.getTransaction().begin();
		Pessoa pokAux = em.find(Pessoa.class, i);
		em.persist(pokAux);
		em.getTransaction().commit();
	}
	
	//Salvar sem lock
	public void salvar(EntityManager em, Pessoa pok) {
		em.getTransaction().begin();
		em.persist(pok);
		em.getTransaction().commit();
	}
	
	//Busca sem lock
	public void search(EntityManager em, Pessoa pok, int amount) {
		String name = pok.getName();
		for(int i = 1; i<amount +1; i++) {
			
			Pessoa pokAux = em.find(Pessoa.class, i);
			pokAux.setName(name + i);
		}
	}

	
	public void salvarExclusiveLock(EntityManager em, Pessoa pok) {
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
