<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset   
PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
         "http://java.sun.com/products/javahelp/helpset_2_0.dtd">

<helpset version="2.0">

  <!-- title -->
  <title>JAnalyse Manuel</title>

  <!-- maps -->
  <maps>
     <homeID>presentation</homeID>
     <mapref location="Map.jhm"/>
  </maps>

  <!-- views -->
  <view mergetype="javax.help.UniteAppendMerge">
    <name>TOC</name>
    <label>Table Des Mati�res</label>
    <type>javax.help.TOCView</type>
    <data>JanalyseTOC.xml</data>
  </view>

  <view mergetype="javax.help.SortMerge">
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>JanalyseIndex.xml</data>
  </view>

  <view mergetype="javax.help.SortMerge">
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>
  
</helpset>

