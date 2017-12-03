package org.davidmoten.rx.jdbc.pool.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.davidmoten.rx.jdbc.internal.DelegatedConnection;
import org.davidmoten.rx.pool.Checkin;

public final class PooledConnection implements DelegatedConnection {

    private final Connection connection;
    private final Checkin checkin;

    public PooledConnection(Connection connection, Checkin checkin) {
        this.connection = connection;
        this.checkin = checkin;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new ConnectionNonBlockingMemberPreparedStatement(con().prepareStatement(sql), this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new ConnectionNonBlockingMemberPreparedStatement(
                con().prepareStatement(sql, columnIndexes), this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return new ConnectionNonBlockingMemberPreparedStatement(
                con().prepareStatement(sql, columnNames), this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return new ConnectionNonBlockingMemberPreparedStatement(
                con().prepareStatement(sql, autoGeneratedKeys), this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new ConnectionNonBlockingMemberPreparedStatement(con().prepareStatement(sql,
                resultSetType, resultSetConcurrency, resultSetHoldability), this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return new ConnectionNonBlockingMemberPreparedStatement(
                con().prepareStatement(sql, resultSetType, resultSetConcurrency), this);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return new ConnectionNonBlockingMemberCallableStatement(con().prepareCall(sql), this);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return new ConnectionNonBlockingMemberCallableStatement(
                con().prepareCall(sql, resultSetType, resultSetConcurrency), this);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return new ConnectionNonBlockingMemberCallableStatement(
                con().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability),
                this);
    }

    @Override
    public void close() {
        // doesn't close the underlying connection, just releases it for reuse
        checkin.checkin();
    }

    @Override
    public Connection con() {
        return connection;
    }

}
