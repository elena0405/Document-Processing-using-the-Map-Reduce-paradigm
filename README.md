# Document-Processing-using-the-Map-Reduce-par
Tema 2 Algoritmi Paraleli si Distribuiti

In cadrul acestei teme, am rezolvat pasii esentiali in clasa Tema2. Am folosit modelul clasic de paralelizare, anume
folosind thread-uri. Astfel, am creat o clasa Task, in care retin informatiile aferente fiecarui task: fisierul,
offset-ul si dimensiunea fragmentului aferent taskului respectiv. Crearea taskurilor am facut-o in metoda main a clasei
Tema2. Apoi, tot in cadrul acestei metode, creez primele thread-uri ,cele pentru etapa de Map, si dau join pe ele.
Fiecare astfel de thread, va lua o bucata din vectorul de taskuri (impartirea am facut-o ca in laboratorul 1), apoi,
pentru fiecare task asociat, va trece peste cuvintele deja citite (adica daca incepe la jumatatea unui cuvant, va sari
 peste acesta) si citi restul fragmentului, dupa care va imparti acest fragment citit in cuvinte. Pentru fiecare astfel
 de cuvant citit, va verifica daca a fost sau nu adaugat in dictionarul aferent taskului respectiv si va face operatiile
 corespunzatoare in fiecare caz, conform cerintelor temei si a explicatiilor din tema. De asemenea, se determina si
 lungimea maxima a cuvintelor din fragment si o lista cu cuvintele care au dimensiunea egala cu acea valoare maxima.
Se retin hasmap-ul creat, lista de cuvinte si fisierul curent intr-o lista de tupluri, partajata de toate thread-urile.
In continuare, se grupeaza, in metoda main, tuplurile aferente aceluiasi fisier. Se creeaza noile thread-uri, pentru
etapa de Reduce, si se da join pe ele. In cadrul acestor thread-uri, grupez toate hashmap-urile din lista de tupluri
intr-un singur hashmap, pentru usurinta calculelor. Calculez rangul conform formulei din tema. Ma intorc in metoda main
si scriu in fisierul de scriere valorile cerute in cerinte temei.
