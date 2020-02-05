"C:\Program Files\Java\jdk1.8.0_31\bin\xjc" -p com.sos.joc.model.commands  -d C:\development_113\products\joc\webservice-global\src\main\java\  "C:\development_113\products\joc\webservice-global\src\test\resources\command-schema.xsd"
echo !!! remove namespace = http://www.sos-berlin.com/scheduler from  com.sos.joc.model.commands.JobschedulerCommands !!!

echo Check that params.get in scheduler.xsd have a complex type like
echo     <xsd:element name="params.get">
echo          <xsd:complexType>
echo             <xsd:attribute name="name" use="required"/>
echo         </xsd:complexType>
echo     </xsd:element>                                                                                                                                                                                                 
 pause