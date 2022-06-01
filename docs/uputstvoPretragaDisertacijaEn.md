# User manual for searching defended PhD dissertations

# 1. Introduction

```
The information system of scientific activity called CRIS UNS (Current Research Information
System of the University of Novi Sad) has been developed for the needs of the University of
Novi Sad. Part of this system is a digital library of doctoral dissertations that fulfills the
obligations defined by the Law on Higher Education in Article 30. The library is used by two
universities:
```
1. University of Novi Sad
2. University Business Academy of Novi Sad.

```
Dissertations data in CRIS UNS software system are in accordance with the requirements
prescribed by the University as well as in accordance with the requirements prescribed by
standards in library and scientific research domain (MARC 21, Dublin Core, ETD-MS, CERIF).
This compatibility with the worldwide accepted standards will ensure that the data from this
system can be exported to and made publicly visible throw the international library systems and
networks repositories of scientific research results such as NDLTD (http://www.ndltd.org/), Оpen
AIRE+ (https://www.openaire.eu/), DART Europe (http://www.dart-europe.eu/basic-search.php).
```
```
This user manual briefly describes basic steps for using the application for searching defended
PhD dissertations. The web page for searching dissertations defended on the University of Novi
Sad is available at the link http://www.cris.uns.ac.rs/searchDissertations.jsf and the web page for
searching dissertations defended on the University Of Business Academy Of Novi Sad is
available at the link http://www.cris.uns.ac.rs/searchDissertationsPA.jsf. User manual for both
Universities is the same. Upcoming text describes user manual for University of Novi Sad.
```
```
Using of some of the following web browsers is recommended:
 Internet Explorer 8.x, installation version can be downloaded from this address
http://www.microsoft.com/windows/internet-explorer/worldwide-sites.aspx,
```
```
 Mozilla Firefox 5.0.x, installation version can be downloaded from this address
http://www.mozilla.com/en-US/firefox/firefox.html.
We hope that application does not have too many flaws. We apologize in advance for any
mistakes in the application. If you notice a mistake, you have a claim, a complaint or any
comment please send us an e-mail by selecting the item in the right corner of the main menu
of our application (cris@uns.ac.rs). Also, if you notice that information about some dissertation
should be fixed, please let us know.
```
```
Thank you for using our application!
CRIS UNS development team
```

# 2. Header of application

```
Header (top) of the application for dissertations search is all time accessible and it is shown in
Figure 1. In addition to the title (Search of dissertations of University of Novi Sad), there are
options to change the user interface language ( / ), send e-mail ( ) to CRIS system
development team, and download this user manual ( ).
```
```
Figure 1. Header of application
```
```
User interface language can be changed by selecting one of the graphical symbols (Serbian)
or (English).
A question or request a user can submit to the development team using the option located in
the right corner of the main application menu.
User manual can be downloaded by selecting the item which is located in the far right corner
of the main application menu.
Searching has three distinct modes which can be opened by selecting one of the following
options Dissertations, Authors and board members and Search based on query language
(Figure 1). These modes are described in the following sections.
```
# 3. Search of dissertations

```
By selecting the option Dissertations the form for making query for dissertations search is
opened. Queries consist of operands that are separated by operators AND, OR or AND NOT
shown in the left corner of the table in Figure 2. Each operand is defined by one row in the table.
After opening a form for searching dissertations there are three operands that are connected by
logical operator AND. Adding and deleting of operands is enabled by selection buttons and
which are located in the right corner of the table in Figure 2.
```
```
Figure 2. Mode for making queries for searching dissertations
```
```
The user can select the field name for searching by choosing an option from the list (Figure 3).
```

```
Figure 3. Fields names
```
The fields Complete document; Title, abstract, keywords; Title; Abstract; Keywords enable four
types of words superposition as it is shown in Figure 4, the default value is has all of this words.

```
Figure 4. Superposition of words for field Title
```

By selecting the field Author from the drop-down list of fields names system offers words
superposition with lastname and firstname. After a typing of couple of letters in input fields,
regardless of whether the characters are in Cyrillic or Latin alphabets, the system offers drop-
down list of researchers which names start with entered letters. Figure 5 shows offered help by
system after entering the lastname Surla. The same kind of help is available when user chooses
one of the following fields Advisor, Committee chairman, Committee member.

```
Figure 5. Drop-down list of researchers after entering lastname Surla
```
Figure 6 shows an example of setting up a more complex query that has two operands. This
query searches dissertations which titles contain words information systems and moreover which
texts contain words software architecture.

```
Figure 6. An example of a more complex query
```
The user can filter results by time period, by the institution where the dissertations are defended
and by gained degree. If the user wants to search for dissertation only in specific period of time,
the user can require it by entering or selecting the year from the drop-down list. Also, filtering
can be done by institutions of the University of Novi Sad where the dissertations are defended.
After selecting some institution, a list of all degree that can be gained by defending dissertation
on that institution is shown.

Figure 7 shows a filtering of list of dissertations that have affiliation Faculty of Technical
Sciences at Novi Sad, University of Novi Sad, which gained degree is Doctor of Technical
Sciences, and which are published from 2005 to 2010.


```
Figure 7. Filtering results
```
```
After pressing the button Search the system sends a query to the server and then displays the
results list.
```
# 4. Search by authors and board members

```
By selecting the option Authors and board members the form for making queries for searching
authors and board members is opened (Figure 8).
```
```
Figure 8. Mode for making queries for searching authors and board members
```
```
The user can enter the firstname and lastname by entering it from keyboard, or as previously
described (text below Figure 4 ) choosing a researcher from drop-down list offered by the
system. Both ways open a table with a list of authors/board members. If the user entered
lastname using keyboard and pressed the Search button the list of authors and board members
with that lastname is shown (Figure 9).
```

```
Figure 9. List of authors with lastname Surla
```
```
If the user has selected authors from the drop-down list (Figure 8) Surla Dušan, the system
displays information about that author (Figure 10). Table in Figure 10 contains basic
information and some additional data such as: Dissertations, Advisor, Committee chairman and
Committee member. Option show in column Dissertations shows list of dissertations written by
Dušan Surla. Other columns options show list of dissertations where Dušan Surla was advisor,
committee chairman or committee member, respectively.
```
```
Figure 10. Researcher Surla Dušan
```
# 5. Search based on the Lucene query language

```
By opening the mode Search based on query language the form for writing Lucene query is
opened. Syntax for Lucene query language is available on address
```

```
http://lucene.apache.org/core/old_versioned_docs/versions/2_9_1/queryparsersyntax.html. List
of available fields for searching are located on the right side of the form. Figure 11 shows the
query written in Lucene query language that retrieves the same results as query created in Figure
6.
```
```
Figure 11. Search based on query language
```
```
After pressing the button Search the system sends a query to the server and then displays the
results list.
```
# 6. Search results

```
Search results are available in form that is shown in Figure 12.
```
```
Figure12. Search results
```
```
The default view of result contains basic information about the dissertation using Harvard style.
By clicking on the button a window with modes Additional data, MARC 21, Dublin Core,
ETD MS and Electronic version is opened. Mode Additional data offers additional information
about dissertation (Figure 13). The window with the additional information can be closed by
clicking on the button for the second time.
```

```
Figure 13. Additional data
```
The next three modes of the window contain information about the dissertation in some of
widely adopted library standards. MARC 21 mode offers display of dissertation in the MARC 21
standard (http://www.loc.gov/marc/bibliographic/), Dublin Core mode offers display of
dissertation in the Dublin Core standard ((http://dublincore.org/documents/dces/), ETD MS
mode offers display of dissertation in ETD MS standard
(http://www.ndltd.org/standards/metadata/etd-ms-v1.00-rev2.html). The last mode Electronic
version shown in Figure 13 enables sending a request to the University library (CBUNS@uns.ac.rs)
for obtaining electronic version of dissertation.


