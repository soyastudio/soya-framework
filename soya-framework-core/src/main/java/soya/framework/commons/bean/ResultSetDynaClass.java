package soya.framework.commons.bean;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetDynaClass extends DynaClassBase {

    private boolean lowerCase = true;
    private boolean useColumnLabel;
    private int limit = -1;

    private Map<String, String> columnNameXref;

    private List<DynaBean> rows = new ArrayList<>();

    public ResultSetDynaClass(final ResultSet resultSet) throws SQLException {
        this(resultSet, true);
    }

    public ResultSetDynaClass(final ResultSet resultSet, final boolean lowerCase)
            throws SQLException {
        this(resultSet, lowerCase, false, -1);

    }

    public ResultSetDynaClass(final ResultSet resultSet, final boolean lowerCase, final boolean useColumnLabel, int limit)
            throws SQLException {
        this.lowerCase = lowerCase;
        this.useColumnLabel = useColumnLabel;
        this.limit = limit;

        introspect(resultSet);
        copy(resultSet);

    }

    protected void introspect(final ResultSet resultSet) throws SQLException {

        // Accumulate an ordered list of DynaProperties
        final ArrayList<DynaProperty> list = new ArrayList<DynaProperty>();
        final ResultSetMetaData metadata = resultSet.getMetaData();
        final int n = metadata.getColumnCount();
        for (int i = 1; i <= n; i++) { // JDBC is one-relative!
            final DynaProperty dynaProperty = createDynaProperty(metadata, i);
            if (dynaProperty != null) {
                list.add(dynaProperty);
            }
        }

        // Convert this list into the internal data structures we need
        setProperties(list.toArray(new DynaProperty[list.size()]));

    }

    protected DynaProperty createDynaProperty(
            final ResultSetMetaData metadata,
            final int i)
            throws SQLException {

        Column column = new Column(
                metadata.getCatalogName(i),
                metadata.getColumnClassName(i),
                metadata.getColumnDisplaySize(i),
                metadata.getColumnLabel(i),
                metadata.getColumnName(i),
                metadata.getColumnType(i),
                metadata.getColumnTypeName(i),
                metadata.getPrecision(i),
                metadata.getScale(i),
                metadata.getSchemaName(i),
                metadata.getTableName(i),
                metadata.isAutoIncrement(i),
                metadata.isCaseSensitive(i),
                metadata.isCurrency(i),
                metadata.isDefinitelyWritable(i),
                metadata.isNullable(i),
                metadata.isReadOnly(i),
                metadata.isSearchable(i),
                metadata.isSigned(i),
                metadata.isWritable(i)
        );

        String columnName = null;
        if (useColumnLabel) {
            columnName = metadata.getColumnLabel(i);
        }
        if (columnName == null || columnName.trim().length() == 0) {
            columnName = metadata.getColumnName(i);
        }

        final String name = lowerCase ? columnName.toLowerCase() : columnName;
        if (!name.equals(columnName)) {
            if (columnNameXref == null) {
                columnNameXref = new HashMap<String, String>();
            }
            columnNameXref.put(name, columnName);
        }
        String className = null;
        try {
            final int sqlType = metadata.getColumnType(i);
            switch (sqlType) {
                case Types.DATE:
                    return new DynaProperty(name, Date.class);
                case Types.TIMESTAMP:
                    return new DynaProperty(name, Timestamp.class);
                case Types.TIME:
                    return new DynaProperty(name, Time.class);
                default:
                    className = metadata.getColumnClassName(i);
            }
        } catch (final SQLException e) {
            // this is a patch for HsqlDb to ignore exceptions
            // thrown by its metadata implementation
        }

        // Default to Object type if no class name could be retrieved
        // from the metadata
        Class<?> clazz = Object.class;
        if (className != null) {
            clazz = loadClass(className);
        }

        DynaProperty property = new DynaProperty(name, clazz);
        property.annotate(Column.class.getName(), column);

        return property;

    }

    protected Class<?> loadClass(final String className) throws SQLException {

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = this.getClass().getClassLoader();
            }
            // use Class.forName() - see BEANUTILS-327
            return Class.forName(className, false, cl);
        } catch (final Exception e) {
            throw new SQLException(
                    "Cannot load column class '" + className + "': " + e);
        }

    }

    protected void copy(final ResultSet resultSet) throws SQLException {
        DynaProperty[] properties = getDynaProperties();
        int cnt = 0;
        while (resultSet.next() && (limit < 0 || cnt++ < limit)) {
            final DynaBean bean = new ResultSetDynaBean(this);
            for (DynaProperty property : properties) {
                final String name = property.getName();
                final Object value = getObject(resultSet, name);
                bean.set(name, value);
            }
            rows.add(bean);
        }
    }

    protected Object getObject(final ResultSet resultSet, final String name) throws SQLException {

        final DynaProperty property = getDynaProperty(name);
        if (property == null) {
            throw new IllegalArgumentException("Invalid name '" + name + "'");
        }
        final String columnName = getColumnName(name);
        final Class<?> type = property.getType();

        // java.sql.Date
        if (type.equals(Date.class)) {
            return resultSet.getDate(columnName);
        }

        // java.sql.Timestamp
        if (type.equals(Timestamp.class)) {
            return resultSet.getTimestamp(columnName);
        }

        // java.sql.Time
        if (type.equals(Time.class)) {
            return resultSet.getTime(columnName);
        }

        return resultSet.getObject(columnName);
    }

    protected String getColumnName(final String name) {
        if (columnNameXref != null && columnNameXref.containsKey(name)) {
            return columnNameXref.get(name);
        } else {
            return name;
        }
    }

    @Override
    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return new ResultSetDynaBean(this);
    }

    public List<DynaBean> getRows() {
        return rows;
    }

    static class ResultSetDynaBean extends DynaBeanBase<ResultSetDynaClass> {
        protected ResultSetDynaBean(ResultSetDynaClass dynaClass) {
            super(dynaClass);
        }
    }

    public static final class Column {
        private final String catalogName;
        private final String columnClassName;
        private final int columnDisplaySize;
        private final String columnLabel;
        private final String columnName;
        private final int columnType;
        private final String getColumnTypeName;
        private final int precision;
        private final int scale;
        private final String schemaName;
        private final String tableName;
        private final boolean autoIncrement;
        private final boolean caseSensitive;
        private final boolean currency;
        private final boolean definitelyWritable;
        private final int nullable;
        private final boolean readOnly;
        private final boolean searchable;
        private final boolean signed;
        private final boolean writable;

        Column(
                String catalogName,
                String columnClassName,
                int columnDisplaySize,
                String columnLabel,
                String columnName,
                int columnType,
                String getColumnTypeName,
                int precision,
                int scale,
                String schemaName,
                String tableName,
                boolean autoIncrement,
                boolean caseSensitive,
                boolean currency,
                boolean definitelyWritable,
                int nullable,
                boolean readOnly,
                boolean searchable,
                boolean signed,
                boolean writable
        ) {
            this.catalogName = catalogName;
            this.columnClassName = columnClassName;
            this.columnDisplaySize = columnDisplaySize;
            this.columnLabel = columnLabel;
            this.columnName = columnName;
            this.columnType = columnType;
            this.getColumnTypeName = getColumnTypeName;
            this.precision = precision;
            this.scale = scale;
            this.schemaName = schemaName;
            this.tableName = tableName;
            this.autoIncrement = autoIncrement;
            this.caseSensitive = caseSensitive;
            this.currency = currency;
            this.definitelyWritable = definitelyWritable;
            this.nullable = nullable;
            this.readOnly = readOnly;
            this.searchable = searchable;
            this.signed = signed;
            this.writable = writable;
        }

        public String getCatalogName() {
            return catalogName;
        }

        public String getColumnClassName() {
            return columnClassName;
        }

        public int getColumnDisplaySize() {
            return columnDisplaySize;
        }

        public String getColumnLabel() {
            return columnLabel;
        }

        public String getColumnName() {
            return columnName;
        }

        public int getColumnType() {
            return columnType;
        }

        public String getGetColumnTypeName() {
            return getColumnTypeName;
        }

        public int getPrecision() {
            return precision;
        }

        public int getScale() {
            return scale;
        }

        public String getSchemaName() {
            return schemaName;
        }

        public String getTableName() {
            return tableName;
        }

        public boolean isAutoIncrement() {
            return autoIncrement;
        }

        public boolean isCaseSensitive() {
            return caseSensitive;
        }

        public boolean isCurrency() {
            return currency;
        }

        public boolean isDefinitelyWritable() {
            return definitelyWritable;
        }

        public int isNullable() {
            return nullable;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public boolean isSigned() {
            return signed;
        }

        public boolean isWritable() {
            return writable;
        }
    }

}
