# ACE Reader for the 2004 and 2005 datasets.

## Overview

Dataset Annotation Guidelines [link] (https://www.ldc.upenn.edu/collaborations/past-projects/ace/annotation-tasks-and-specifications)

Dataset Download links:
[ACE-2004](http://cogcomp.cs.illinois.edu/page/resource_view/60)
[ACE-2005](http://cogcomp.cs.illinois.edu/page/resource_view/59)

## Implementation details

Each document is read into a [`TextAnnotation`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/TextAnnotation.java) instance with the following views defined in the [ViewNames class](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/ViewNames.java):

  - **`TOKENS`**: The basic [`TokenLabelView`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/TokenLabelView.java) view generated by a `Tokenizer` from the raw dataset
  text
  - **`NER_ACE_COARSE_HEAD, NER_ACE_COARSE_EXTENT`**: [`SpanLabelView`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/SpanLabelView.java) with overlapping constituents where each constituent 
  represents a entity extent and head-word information is stored as attributes and the label is the Coarse Entity Type.
  - **`NER_ACE_FINE_HEAD, NER_ACE_FINE_EXTENT`**: [`SpanLabelView`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/SpanLabelView.java) with overlapping constituents where each constituent 
  represents a entity extent and head-word information is stored as attributes and the label is the Fine Entity Type. If the Fine Entity type is not available, we fall-back to the coarse type for these entities.
  - **`RELATION_ACE_COARSE_HEAD, RELATION_ACE_COARSE_EXTENT`**: [`PredicateArgumentView`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/PredicateArgumentView.java) for storing relations. Predicate represents "Arg-1" and its corresponding Argument represents "Arg-2" of the ACE Relation. For entities with multiple outgoing relations, we add duplicate copies of the relation source into predicates. Thus, number of predicates is equal to the number of relations in the gold annotation. Relation name is the coarse relation between `Predicate` and its `Argument`.
  - **`RELATION_ACE_FINE_HEAD, RELATION_ACE_FINE_EXTENT`**: [`PredicateArgumentView`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/PredicateArgumentView.java) for storing relations. Same as `ViewNames.RELATION_ACE_COARSE` view but each relation has its fine relation type as the relation name.
  - **`COREF_HEAD, COREF_EXTENT`**: [`CoreferenceView`](../../core-utilities/src/main/java/edu/illinois/cs/cogcomp/core/datastructures/textannotation/CoreferenceView.java) uses copies of mentions from `NER_ACE_COARSE_*` views and adds the longest mention as the canonical mention + adds coreference edges to other mentions for the same entity. 

In the `*_HEAD` views, the constituents are **heads** of the mentions and the contituents in the `*_EXTENT` views constain **extents** of the mentions accordingly.

## Usage

### Directory Structure

The reader expects data-set to be in the following structure:

```
corpusHomeDir
├── bc
│   └── apf.dtd
|   └── <other files (*.apf.xml, *.sgm)>
├── bn
│   └── apf.dtd
|   └── <other files (*.apf.xml, *.sgm)>
├── cts
│   └── apf.dtd
|   └── <other files (*.apf.xml, *.sgm)>
└── newswire_nw
    └── apf.dtd
    └── <other files (*.apf.xml, *.sgm)>
```

Each of the sub-directories represents a **section** and has different text parsing logic. The reader expects the section directories to end with a suffix representing the parser to be used according to the following suffix logic:

- **bn** : Broadcast News
- **nw** : Newswire
- **bc** : Broadcast Conversation
- **wl** : Weblog
- **un** : Usenet Newsgroups/Discussion Forum
- **cts** : Conversational Telephone Speech

### Java Usage

```java
import edu.illinois.cs.cogcomp.nlp.corpusreaders.ACEReader;

// Read all sections in ACE-2004
ACEReader reader2004 = new ACEReader("data/ace2004/data/English", true);

// Read all sections in ACE-2005
ACEReader reader2005 = new ACEReader("data/ace2005/data/English", false);

// Read limited sections only
String[] sections = new String[] { "nw", "bn" };
ACEReader reader2004Partial = new ACEReader("data/ace2004/data/English", sections, true);
```

`ACEReader` implements `Iterable<TextAnnotation>` interface.

Sample Usage:

```java
while (reader.hasNext()) {
	TextAnnotation doc = reader.next();
	...
}
```
or

```java
for (TextAnnotation doc : reader) {
	...
}
```

### Caveats

- Values, TimeEX and Events are not parsed currently.