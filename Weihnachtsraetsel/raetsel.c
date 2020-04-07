/*      ************************                                                                                                            
        * (C) Maximilian Fries *                                                                                                            
        *  OTH Regensburg IN1  *                                                                                                            
        *      25.12.2015      *                                                                                                            
        *************************                                                                                                           
*/                                                                                                                                          
                                                                                                                                            
#include <stdio.h> include <math.h>                                                                                                         
                                                                                                                                            
void print_array(int ar[], int * size) {                                                                                                    
    int i;                                                                                                                                  
    for (i = 0; i < *size; i++)                                                                                                             
        printf("%d ", ar[i]);                                                                                                               
    printf("\n");                                                                                                                           
}                                                                                                                                           
                                                                                                                                            
int main() {                                                                                                                                
    int size;                                                                                                                               
    int verbose = 0;                                                                                                                        
                                                                                                                                            
    printf("Verbose Modus? [2|1|0], default: 0\n");                                                                                         
    scanf("%d", &verbose);                                                                                                                  
                                                                                                                                            
    if (verbose != 0)                                                                                                                       
        printf("Anzahl der Iterationen angeben: (max. 1.000)\n");                                                                           
    else                                                                                                                                    
        printf("Anzahl der Iterationen angeben: (max. 2.000.000)\n");                                                                       
    scanf("%d", &size);                                                                                                                     
                                                                                                                                            
    int ar[size];                                                                                                                           
    int i, j;                                                                                                                               
                                                                                                                                            
    for (i = 1; i < size + 1; i++)                                                                                                          
    {                                                                                                                                       
        int k;                                                                                                                              
        for (j = 1; (k = i*j-1) < size; j++)                                                                                                
        {                                                                                                                                   
            if(i == 1)                                                                                                                      
                ar[k] = 1; // Initialisierung                                                                                               
            else                                                                                                                            
                ar[k] = 1 - ar[k]; // Umschalten                                                                                            
        }                                                                                                                                   
        if (verbose == 2 || verbose == 1 && i == size)                                                                                      
        {                                                                                                                                   
            printf("%4d. Engel ", i); // Formatierung                                                                                       
            print_array(ar, &size);                                                                                                         
        }                                                                                                                                   
    }                                                                                                                                       
    printf("\nHausnummern mit Licht: ");                                                                                                    
    for (i = 0; i < size; i++)                                                                                                              
    {                                                                                                                                       
        if(ar[i])                                                                                                                           
            printf("%d, ", i + 1);                                                                                                          
    }                                                                                                                                       
    printf("\n");                                                                                                                           
    return 0;                                                                                                                               
}  
