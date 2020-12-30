# JLON library

Semestral project JLON for BIK-OOP.

### Checklist:

* *you don’t need to worry about an escape sequences in strings (nor names nor values)*<br/>
This implementation **supports escaping** with backslash - like "Quote \\" or backslash \\\\ inside of string".

* *all prop names can not contain a dot symbol “.” - this is because querying would get unnecessary complicated*<br/>
Prop names **may contain** dot "." symbol which can be escaped with backslash in queries.

* (i implemented the two features above because without them the whole thing is hardly usable in real world)

* *Parsing a JLON input*<br/>
Implemented. Parsed JLON is consisted from own custom classes.

* *Querying the value*<br/>
Querying values is possible with returning of the correct type. It may return JLON structure casted to JLON string. With **.getRef** it returns reference to JLON structure or single element.

* *Extend the query to work on lists*<br/>
Its possible to query specific index on lists / arrays. It works in same way as querying on properties.

* *Parse the JLON value lazily*<br/>
Its implemented. The parsing logic is only one - but the non-lazy JLON enforce parsing of all childs and sub-childs. When composed as **.toJLON**, than the unparsed sub-childs remain untouched even with errors.

* *extend the parser to also work when the top level value is any other valid JLON value*<br/>
It works - top level elements might be number, string, boolean (on **.get()** it returns that value) or list (on **.get("2")** it returns third element).

* *add method set which will either add to or modify specified property of the JLON value*<br/>
Its implemented. Supported to set primitive types or unparsed JLON fragment. Its also possible to set in any depth of new properties like **.set("new1.new2.new3", 10)**.

* *implement pretty printing for the JLON string to the stream or string with indentation*<br/>
Implemented **.toJLONpretty** with identation.

* *Because you are writing a parser and a library which could potentially be used in other projects, testing is a necessity!*<br/>
Various of tests are created.

#### Additional features:

* Exception at invalid JLON input provide context where error occurs.

* Possible **.get** chaining like **jlon.get("prop_a").get("prop_b")** .

#### Idea

Base class of JLON entry is **JEntry** which holds value of specific type or substructure and provide necessary abstract interface.

Subclasses of primitive types is **JPrimBoolean**, **JPrimString**, **JPrimNumber**.

If **JEntry** holds substructure (object or array) it must be subclass of **JCont** of desired key type as *Int* (for arrays) or *String* (for objects). 
Subclass of **JCont** for arrays is **JArray** and for objects is **JObject**.

For unparsed JLON are provided **JData** and **JDataLazy** classes, which are also subclasses of **JEntry**. 
The **JData** class provides same interface as **JEntry** (for querying etc) and holds the instance of final **JEntry** with parsed data / structure.

For lazy JLON processing is **JDataLazy** subclass. 
This lazy class keeps uparsed parts of JLON as long as possible - even when **.toJLON** is called, the untouched parts remains unparsed.

Detailed parsing logic is provided by **JParser** classes.

Skipping logic is in **ListScanner.readInnerContentToBufferTill**.
