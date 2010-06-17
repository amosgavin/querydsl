/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;

/**
 * Query interface for SQL queries
 * 
 * @author tiwe
 *
 */
public interface SQLQuery extends Query<SQLQuery>, Projectable {
  
    /**
     * Defines the sources of the query
     * 
     * @param o
     * @return
     */
    SQLQuery from(Expr<?>... o);

    /**
     * Adds a full join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery fullJoin(PEntity<?> o);

    /**
     * Adds an inner join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery innerJoin(PEntity<?> o);

    /**
     * Adds a join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery join(PEntity<?> o);
    
    /**
     * Adds a left join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery leftJoin(PEntity<?> o);
    
    /**
     * Adds a right join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery rightJoin(PEntity<?> o);
    
    /**
     * Adds a full join to the given target
     * 
     * @param o
     * @return
     */
    <E, P> SQLQuery fullJoin(ForeignKey<E,P> foreign, Key<E,P> primary);
    
    /**
     * Adds an inner join to the given target
     * 
     * @param o
     * @return
     */
    <E, P> SQLQuery innerJoin(ForeignKey<E,P> foreign, Key<E,P> primary);

    /**
     * Adds a join to the given target
     * 
     * @param o
     * @return
     */
    <E, P> SQLQuery join(ForeignKey<E,P> foreign, Key<E,P> primary);
    
    /**
     * Adds a left join to the given target
     * 
     * @param o
     * @return
     */
    <E, P> SQLQuery leftJoin(ForeignKey<E,P> foreign, Key<E,P> primary);
    
    /**
     * Adds a right join to the given target
     * 
     * @param o
     * @return
     */
    <E, P> SQLQuery rightJoin(ForeignKey<E,P> foreign, Key<E,P> primary);
    
    /**
     * Adds a full join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery fullJoin(SubQuery<?> o, Path<?> alias);

    /**
     * Adds an inner join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery innerJoin(SubQuery<?> o, Path<?> alias);

    /**
     * Adds a join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery join(SubQuery<?> o, Path<?> alias);
    
    /**
     * Adds a left join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery leftJoin(SubQuery<?> o, Path<?> alias);
    
    /**
     * Adds a right join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery rightJoin(SubQuery<?> o, Path<?> alias);
    
    /**
     * Defines a filter to the last added join
     * 
     * @param conditions
     * @return
     */
    SQLQuery on(EBoolean... conditions);
    
    /**
     * Creates an union expression for the given subqueries
     * 
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(ListSubQuery<RT>... sq);
    
    /**
     * Creates an union expression for the given subqueries
     * 
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(SubQuery<RT>... sq);
    
    /**
     * Clone the state of the Query for the given Connection
     * 
     * @param conn
     * @return
     */
    SQLQuery clone(Connection conn);

    /**
     * Get the results as an JDBC result set
     * 
     * @param args
     * @return
     */
    ResultSet getResults(Expr<?>... args);
}
