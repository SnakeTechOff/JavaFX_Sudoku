package environment;

public class Tuple<I, J> { 
	  public final I i; 
	  public final J j; 
	  public Tuple(I i, J j) { 
	    this.i = i; 
	    this.j = j; 
	  } 
	  
	  public final I getI() {
		  return this.i;
	  }
	  
	  public final J getJ() {
		  return this.j;
	  }
} 
