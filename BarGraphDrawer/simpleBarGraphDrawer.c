#include <stdio.h>
int val[4] = {5, 3, -8, 4};
void prnt(){ printf("#\t");return; }
void tab(){ printf(" \t"); }

int main(){
  int min, max = 10; min = -1*max;
  int i,j;
  for(i = max; i >= min; --i){
	  printf("%3d ", i);
	  if (i == 0){
		  printf("a\tb\tc\td\n");
		  continue;
	  }
    for (j = 0; j < 4; j++)
		i >= 0 ? (i <= val[j] ? prnt() : tab())
				: (val[j] > i ? tab() : prnt());
   printf("\n");
  }
  printf("\n");
  return 0;
}
