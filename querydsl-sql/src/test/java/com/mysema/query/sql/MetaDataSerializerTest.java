package com.mysema.query.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.SimpleCompiler;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;

public class MetaDataSerializerTest {    

    private Connection conn;
    
    private Statement stmt;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:data/tutorial";
        conn = DriverManager.getConnection(url, "sa", "");
        stmt = conn.createStatement();
    }
    
    @After
    public void tearDown() throws SQLException{
        try{
            stmt.close();    
        }finally{
            conn.close();    
        }
    }
    
    @Test
    public void testGeneration() throws Exception {
        // normal settings
        
        stmt.execute("drop table employee if exists");        
        stmt.execute("drop table survey if exists");
        stmt.execute("drop table date_test if exists");
        stmt.execute("drop table date_time_test if exists");
                
        stmt.execute("create table survey (id int, name varchar(30), "
                + "CONSTRAINT PK_survey PRIMARY KEY (id))");        
        stmt.execute("create table date_test (d date)");        
        stmt.execute("create table date_time_test (dt datetime)");        
        stmt.execute("create table employee("
                + "id INT, "
                + "firstname VARCHAR(50), " 
                + "lastname VARCHAR(50), "
                + "salary DECIMAL(10, 2), " 
                + "datefield DATE, "
                + "timefield TIME, "
                + "superior_id int, " 
                + "survey_id int, "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_survey FOREIGN KEY (survey_id) REFERENCES survey(id), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");
        
        
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        // customization of serialization
        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy){
            
            @Override
            protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        	super.introImports(writer, config, model);
        	// adds additional imports
        	writer.imports(List.class, Arrays.class);
            }
            
            @Override
            protected void serializeProperties(EntityType model,  SerializerConfig config, CodeWriter writer) throws IOException {
        	super.serializeProperties(model, config, writer);
        	StringBuilder paths = new StringBuilder();
        	for (Property property : model.getProperties()){
        	    if (paths.length() > 0){
        		paths.append(", ");
        	    }
        	    paths.append(property.getEscapedName());
        	}
        	// adds accessors for all fields
        	writer.publicFinal("List<Expr<?>>", "exprs", "Arrays.<Expr<?>>asList(" + paths.toString() + ")");
        	writer.publicFinal("List<Path<?>>", "paths", "Arrays.<Path<?>>asList(" + paths.toString() + ")");        	
            }
            
        };
        MetaDataExporter exporter = new MetaDataExporter(
        	namePrefix, 
        	"test", 
        	null, 
        	null, 
        	new File("target/cust"), 
        	namingStrategy, 
        	serializer);
        
        exporter.export(conn.getMetaData());   
        
        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, null, null, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }
    
}
