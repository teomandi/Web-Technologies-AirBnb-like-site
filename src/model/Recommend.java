package model;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;


import info.debatty.java.lsh.LSHMinHash;


public class Recommend {
	private final DataQuery query=new DataQuery();
	List<User> users;
	List<Room> rooms;
	
	boolean[] user_vector;
	int[] user_vectorHash;
	
	boolean[][] vectors;
	int[][] users_bucket;
	int user_pos;
	int[] user_recommends;
	
	
	//kanei arxikopoihseis..
	public void CreateVectors() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		System.out.println("recommend");
		users=query.find_all();
		rooms=query.find_all_rooms();
		vectors = new boolean[users.size()][rooms.size()];
		
		for(int i=0; i<users.size(); i++) {
			for(int j=0;j<rooms.size(); j++) {
				if( query.findRating(users.get(i), rooms.get(j))<=3 ) 
					vectors[i][j]=false;
				else
					vectors[i][j]=true;
						
			}
			if(session.getAttribute("username").toString().equals(users.get(i).getUsername())) {
				user_vector=vectors[i];
			}
			if(session.getAttribute("recommended")!=null)
				session.removeAttribute("recommended");
		}
	}
	
	
	/*vriskei to ta dwmatia pou tha ginoun recommended
	 * 
	 * sthn arxi efarmozei lsh kai xwrizei tous xrhstes se buckets
	 * epeita vriskei tous xristes pou einai sto idio bucket me ton sundedemeno
	 * 
	 * sthn sunexeia efarmozetai h centered similarity opou vriskoume to mesw oro ths grammhs
	 * kai ton afairoume apo kathe oro
	 * 
	 * Sth sunexeia vriskoume to cosine similarity kai dialegoume ton enan h tous 2 xristes pou 
	 * exoun th mikroterh cs (apoluth timh)..kai epeita upologizoume to upotithemeno rating
	 * me vash tis vathmologies pou exoun dwsei oi paromoioi xristes se dwmtatia pou den exei
	 * episkeutei o sundedemenos xrhsths..sth sunexeia vriskei ta top 3-4 kai ta stelnei sto 
	 * reults opou ta apeikonizei
	 * 
	 * epeidh exoume perasei liga dedomena xrisimopoioume liga buckets kai liga stadia sto 
	 * lsgh 
	 * 
	 * 
	 */
	public List<Room> recommendation() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		//efarmozei ton lhs
		int stages=2;
		int buckets=2;
		LSHMinHash lsh = new LSHMinHash(stages, buckets, rooms.size());
		int[][] hash=new int[users.size()][];
		
		user_vectorHash=lsh.hash(user_vector);
		int counter=0;
		for (int j=0; j<users.size();j++) {
            hash[j] = lsh.hash(vectors[j]);
            if(Arrays.equals(hash[j],user_vectorHash))
            	counter++;
        }
			
		if(counter>1) {
			
			//briskei kai ftiaxnei to bucket pou periexei tous omoious tou
			user_vectorHash=lsh.hash(user_vector);
			users_bucket=new int[counter][rooms.size()];
			int k=0;
			for(int i=0; i<users.size(); i++) {
				if(Arrays.equals(hash[i], user_vectorHash)) {
					//briskei se poia thesh einai o user ston pinaka
					if(users.get(i).getUserId()==(query.findUser(session.getAttribute("username").toString()).getUserId())) {
						user_pos=k;
						System.out.println("eurikaa!");
					}
					
					for(int j=0; j<rooms.size();j++) 
						users_bucket[k][j] = query.findRating(users.get(i), rooms.get(j));
					k++;
				}		
			}
			//ektupwnei to bucket
			System.out.println("------BUCKET-------");
			for (int i=0; i<counter;i++) {
				System.out.print("[");
				for(int j=0; j<rooms.size();j++) 
					System.out.print(" "+users_bucket[i][j]+" ");
				System.out.print("]\n");
			}
			
			
			//ypologizei ton mesw oro gia thn kathe grammh 
			//xreiazetai gia thn centered cosine
			float[] avg_row=new float[counter];
			for (int i=0; i<counter;i++) {
				int elements=0;
				int sum=0;
				for(int j=0; j<rooms.size();j++) {
					if(users_bucket[i][j]>0) {
						sum=sum+users_bucket[i][j];
						elements++;
					}
				}
				if(elements>0)
					avg_row[i]=sum/elements;
				else 
					avg_row[i]=0;
				
				System.out.println(avg_row[i]);
				
			}
			
			//dhmiourgoume pinaka me tis centered cosines
			//opou einai rating-avg_row
			double[][] cc_matrix=new double[counter][rooms.size()];
			for (int i=0; i<counter;i++) {
				for(int j=0; j<rooms.size();j++) {
					if(users_bucket[i][j]>0) {
						cc_matrix[i][j]=users_bucket[i][j]-avg_row[i];
					}
					else
						cc_matrix[i][j]=0;
					}
			}
			System.out.println("------CENTERED COSINES-------");
			for (int i=0; i<counter;i++) {
				System.out.print("[");
				for(int j=0; j<rooms.size();j++) 
					System.out.print(" "+cc_matrix[i][j]+" ");
				System.out.print("]\n");
			}
			
			//upologizei thn cosine similarity gia thn kath grammh me thn grammh tou
			//sundedemenou xrhsth
			double[] cosines_sim=new double[counter-1];
			k=0;
			for (int i=0; i<counter;i++) {
				if(i!=user_pos) {
					cosines_sim[k]=cosineSimilarity(cc_matrix[i],cc_matrix[user_pos]);
					System.out.println("cs: "+cosines_sim[k]);
					k++;
				}
				
			}
			
			//ama einai panw apo 3 dialegei 2 omoious xristes
			if(counter>=3) {
				 double minValue_1 = cosines_sim[0];
				 double minValue_2 = cosines_sim[1];
				 int min_pos_1=0;
				 int min_pos_2=1;
				 for (int i = 1; i < cosines_sim.length; i++) {
				   	if(cosines_sim[i]==0)continue;
				    if (cosines_sim[i] <= minValue_1) {
				    	   minValue_1 = cosines_sim[i];
				    	   min_pos_1=i;
				    }
				    else if(cosines_sim[i] < minValue_2){
				    	minValue_2 = cosines_sim[i];
				    	min_pos_2=i;
				    }
				 }
				 
				 System.out.println("min: "+minValue_1+" pos: "+min_pos_1+"\nmin2: "+minValue_2+" pos2: "+min_pos_2);
				 user_recommends= new int[rooms.size()];
				 for(int j=0; j<rooms.size(); j++) {
					 if(users_bucket[user_pos][j]>0) {
						 //toxei episkeuthei
						 user_recommends[j]=0;
					 }
					 else
						 user_recommends[j]= (int) ((int)(users_bucket[min_pos_1][j]*cosines_sim[min_pos_1]+users_bucket[min_pos_2][j]*cosines_sim[min_pos_2])/(cosines_sim[min_pos_2]+cosines_sim[min_pos_1])) ;
						 
				 }
				 
				
			}
			//alliws enan
			else {
				 double minValue = cosines_sim[0];
				 int min_pos=0;
				 for (int i = 1; i < cosines_sim.length; i++) {
				   	if(cosines_sim[i]==0)continue;
				    if (cosines_sim[i] < minValue) {
				       minValue = cosines_sim[i];
				       min_pos=i;
				    }
				 }
				 
				 System.out.println("min:"+minValue);
				 user_recommends= new int[rooms.size()];
				 for(int j=0; j<rooms.size(); j++) {
					 if(users_bucket[user_pos][j]>0) {
						 //toxei episkeuthei
						 user_recommends[j]=0;
					 }
					 else
						 user_recommends[j]= (int)users_bucket[min_pos][j] ;
						 
				 }
				
			}						
			System.out.println("------recommendS-------");
			for(int j=0; j<rooms.size(); j++) 
				System.out.println(user_recommends[j]+"---"+rooms.get(j).getRoomName());
			
			
			//dialegei ta 4 top!
			List<Room> recommended_rooms=new ArrayList<Room>();
			for (int j = 0; j < 4; j++) {
				boolean flag=false;
		       int max = user_recommends[0];
		       if(max>2)flag=true;
		       int index = 0;
		        for (int i = 1; i < user_recommends.length; i++) {
		        	//theloume na xei dwsei kalh vathmologia
		        	if ( user_recommends[i]>2)continue; 
		            if (max < user_recommends[i]) {
		            	flag=true;
		                max = user_recommends[i];
		                index = i;
		            }
		        }
		        if(flag)
		        	recommended_rooms.add(rooms.get(index));
		        user_recommends[index] = 0;
			}
			System.out.print("[");
			for(int j=0; j<recommended_rooms.size(); j++) 
				System.out.print(" . "+recommended_rooms.get(j).getRoomName()+" . ");
			System.out.print("]\n");
			System.out.println("~~~~END~~~~~");
			
			if(recommended_rooms.size()>0)
				return recommended_rooms ;
			else 
				return null;

		}
		else {
			System.out.println("NO SIMILAR USERS!");
			return null;
		}
		
		
		
	}
    

	
	
	
	
	
	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    if((Math.sqrt(normA) * Math.sqrt(normB))==0)
	    	return 1;
	    else
	    	return Math.abs(dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
	    
	}
	
	public static double getMinValue(double[] array) {
	    double minValue = array[0];
	    for (int i = 1; i < array.length; i++) {
	    	if(array[i]==0)continue;
	        if (array[i] < minValue) {
	            minValue = array[i];
	        }
	    }
	    return minValue;
	}
	
	
    static void print(int[] array) {
        System.out.print("[");
        for (int v : array) {
            System.out.print("" + v + " ");
        }
        System.out.print("]");
    }
    
    static void print(boolean[] array) {
        System.out.print("[");
        for (boolean v : array) {
            System.out.print(v ? "1" : "0");
        }
        System.out.print("]");
    }
}
