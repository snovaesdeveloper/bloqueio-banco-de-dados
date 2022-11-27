package app;

import java.util.ArrayList;
import java.util.Date;

public class MyCounter {
    private int count;
	private ArrayList <Date> lista = new ArrayList<Date>();
	private ArrayList <Integer> lista1 = new ArrayList<Integer>();

    public void increment() {
        int temp = count;
        count = temp + 1;
        
        //Thread.sleep(2);
//        if(count<10 && count>5){
//          //Thread.sleep(2);
//          
//        }
        
        int a[][]={{1,1,1},{2,2,2},{3,3,3}};    
        int b[][]={{1,1,1},{2,2,2},{3,3,3}};    
            
        //creating another matrix to store the multiplication of two matrices    
        int c[][]=new int[3][3];  //3 rows and 3 columns  
            
        //multiplying and printing multiplication of 2 matrices    
        for(int i=0;i<3;i++){    
        for(int j=0;j<3;j++){    
        c[i][j]=0;      
        for(int k=0;k<3;k++)      
        {      
        c[i][j]+=a[i][k]*b[k][j];      
        }//end of k loop  
        ///System.out.print(c[i][j]+" ");  //printing matrix element  
        }//end of j loop  
        //System.out.println();//new line    
        }
        
            
            
        //creating another matrix to store the multiplication of two matrices    
         c=new int[3][3];  //3 rows and 3 columns  
            
        //multiplying and printing multiplication of 2 matrices    
        for(int i=0;i<3;i++){    
        for(int j=0;j<3;j++){    
        c[i][j]=0;      
        for(int k=0;k<3;k++)      
        {      
        c[i][j]+=a[i][k]*b[k][j];      
        }//end of k loop  
        //System.out.print(c[i][j]+" ");  //printing matrix element  
        }//end of j loop  
        //System.out.println();//new line    
        }
      //creating another matrix to store the multiplication of two matrices    
        c=new int[3][3];  //3 rows and 3 columns  
           
       //multiplying and printing multiplication of 2 matrices    
       for(int i=0;i<3;i++){    
       for(int j=0;j<3;j++){    
       c[i][j]=0;      
       for(int k=0;k<3;k++)      
       {      
       c[i][j]+=a[i][k]*b[k][j];      
       }//end of k loop  
       //System.out.print(c[i][j]+" ");  //printing matrix element  
       }//end of j loop  
       //System.out.println();//new line    
       }
     //creating another matrix to store the multiplication of two matrices    
       c=new int[3][3];  //3 rows and 3 columns  
          
      //multiplying and printing multiplication of 2 matrices    
      for(int i=0;i<3;i++){    
      for(int j=0;j<3;j++){    
      c[i][j]=0;      
      for(int k=0;k<3;k++)      
      {      
      c[i][j]+=a[i][k]*b[k][j];      
      }//end of k loop  
      //System.out.print(c[i][j]+" ");  //printing matrix element  
      }//end of j loop  
      //System.out.println();//new line    
      }
        Date data = new Date();
        
        this.lista1.add(count);
        this.lista.add(data);
    }
    public int getCount(){
      return this.count;
    }
	public ArrayList <Date> getLista() {
		return lista;
	}
	public void setLista(ArrayList <Date> lista) {
		this.lista = lista;
	}
	public ArrayList <Integer> getLista1() {
		return lista1;
	}
	public void setLista1(ArrayList <Integer> lista1) {
		this.lista1 = lista1;
	}
}
