package app;
import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Pokemon implements Serializable{
		private static final long serialVersionUID = 1L;
		
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		
		private Integer id;
		private String name;
		//private List<String> moves;
//		private String gender;
//		private Float height;
//		private String weight;
//		//private List<Type> type;
//		//@OneToMany(targetEntity=Pokemon.class, mappedBy="pokemon", fetch=FetchType.EAGER)
//		@ElementCollection
//		private List<String> type;
//		@ElementCollection
//		private List<String> weaknesses;
	  
	  //Stats
	  //moves


	  // public empty constructor needed for retrieving the POJO
	  public Pokemon() {
	  }
	  
	 
//	  public Pokemon(Integer id, String name, String gender, Float height, List<String> type, List<String> weaknesses) {
//		    //this.id = number;
//		  	super();
//		    this.id = id;
//		    this.name = name;
//		    this.gender = gender;
//		    this.height = height;
//		    //this.weight = weight;
//		    this.type = type;
//		    this.weaknesses = weaknesses;
//	    
//	  }
	  
	  public Pokemon(Integer id, String name) {
		    //this.id = number;
		  	super();
		    this.id = id;
		    this.name = name;
	  }
	  

	  public Integer getId() {
	    return id;
	  }

	  public void setId(Integer id) {
	    this.id = id;
	  }

	  
	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	 
	  @Override
	  public String toString() {
	    return "Pokemon [id=" + id + ", name=" + name + "]";
	  }
	}