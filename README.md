# game1
Super Mario Bros. like game
********************************************************************************************************
Genetic Algorithm (general)
 - Consider the set of commands (up, up left, up right, left, right, neutral) as a genes.
 - Alternate commands per several frames
 - Score calculated based on the distance travelled
********************************************************************************************************
Simple Genetic Algorithm (GA)
 - Have several character as 1 generation (eg. 20 characters)
 1. Randomly generate characters for the first generation
 2. Test all characters in a generation
 3. Choose best 10 characters
 4. Choose two characters and choose two point randomly and exchage the genes in between. (cross)
 5. With certain probability (eg. 2%) change the command of each gene. (mutation)
 6. Produce new 20 characters as new generation from old generation
 7. Repeat 2 - 7.
 ********************************************************************************************************
Parameter Free Genetic Algorithm (PFGA)
 - Have several characters as group. The number of characters changes.
 1. Randomly generate a character and put into the group.
 2. Pick two character from the group. If there is only one character in the group, randomly generate one.
 3. Randomly decide the number of point crosses and the place crosses
 4. Generate two new characters (children) by crossing two old characters (parents).
 5. Randomly pick new character, randomly decide the number of mutation, and randomly mutate the gene.
 6. Test 4 characters and return several characters to the group with following rules.
   > If two children are both better than two parents -> Return two children and a better parent
   > If two parents are both better than two children -> Return a better parent
   > If only one parent is better than two childen -> Return a better child and a better parent
   > If only one child is better than two parents -> Return a better child and randomly generated character
 7. Repeat 2 - 7.



