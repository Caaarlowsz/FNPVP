package com.zalpha.FantasyNetwork.PvP.util.storage;

import java.sql.Connection;

public interface Sql
{
    Connection getConnection();
    
    void execute(final String p0, final boolean p1, final Object... p2);
}
