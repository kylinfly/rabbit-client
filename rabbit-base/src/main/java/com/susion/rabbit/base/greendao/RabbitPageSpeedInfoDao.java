package com.susion.rabbit.base.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.susion.rabbit.base.entities.RabbitPageSpeedInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RABBIT_PAGE_SPEED_INFO".
*/
public class RabbitPageSpeedInfoDao extends AbstractDao<RabbitPageSpeedInfo, Long> {

    public static final String TABLENAME = "RABBIT_PAGE_SPEED_INFO";

    /**
     * Properties of entity RabbitPageSpeedInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PageName = new Property(1, String.class, "pageName", false, "PAGE_NAME");
        public final static Property Time = new Property(2, long.class, "time", false, "TIME");
        public final static Property CreateStartTime = new Property(3, long.class, "createStartTime", false, "CREATE_START_TIME");
        public final static Property CreateEndTime = new Property(4, long.class, "createEndTime", false, "CREATE_END_TIME");
        public final static Property InflateFinishTime = new Property(5, long.class, "inflateFinishTime", false, "INFLATE_FINISH_TIME");
        public final static Property FullDrawFinishTime = new Property(6, long.class, "fullDrawFinishTime", false, "FULL_DRAW_FINISH_TIME");
        public final static Property ResumeEndTime = new Property(7, long.class, "resumeEndTime", false, "RESUME_END_TIME");
        public final static Property ApiRequestCostString = new Property(8, String.class, "apiRequestCostString", false, "API_REQUEST_COST_STRING");
    }


    public RabbitPageSpeedInfoDao(DaoConfig config) {
        super(config);
    }
    
    public RabbitPageSpeedInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RABBIT_PAGE_SPEED_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PAGE_NAME\" TEXT," + // 1: pageName
                "\"TIME\" INTEGER NOT NULL ," + // 2: time
                "\"CREATE_START_TIME\" INTEGER NOT NULL ," + // 3: createStartTime
                "\"CREATE_END_TIME\" INTEGER NOT NULL ," + // 4: createEndTime
                "\"INFLATE_FINISH_TIME\" INTEGER NOT NULL ," + // 5: inflateFinishTime
                "\"FULL_DRAW_FINISH_TIME\" INTEGER NOT NULL ," + // 6: fullDrawFinishTime
                "\"RESUME_END_TIME\" INTEGER NOT NULL ," + // 7: resumeEndTime
                "\"API_REQUEST_COST_STRING\" TEXT);"); // 8: apiRequestCostString
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RABBIT_PAGE_SPEED_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RabbitPageSpeedInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String pageName = entity.getPageName();
        if (pageName != null) {
            stmt.bindString(2, pageName);
        }
        stmt.bindLong(3, entity.getTime());
        stmt.bindLong(4, entity.getCreateStartTime());
        stmt.bindLong(5, entity.getCreateEndTime());
        stmt.bindLong(6, entity.getInflateFinishTime());
        stmt.bindLong(7, entity.getFullDrawFinishTime());
        stmt.bindLong(8, entity.getResumeEndTime());
 
        String apiRequestCostString = entity.getApiRequestCostString();
        if (apiRequestCostString != null) {
            stmt.bindString(9, apiRequestCostString);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RabbitPageSpeedInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String pageName = entity.getPageName();
        if (pageName != null) {
            stmt.bindString(2, pageName);
        }
        stmt.bindLong(3, entity.getTime());
        stmt.bindLong(4, entity.getCreateStartTime());
        stmt.bindLong(5, entity.getCreateEndTime());
        stmt.bindLong(6, entity.getInflateFinishTime());
        stmt.bindLong(7, entity.getFullDrawFinishTime());
        stmt.bindLong(8, entity.getResumeEndTime());
 
        String apiRequestCostString = entity.getApiRequestCostString();
        if (apiRequestCostString != null) {
            stmt.bindString(9, apiRequestCostString);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RabbitPageSpeedInfo readEntity(Cursor cursor, int offset) {
        RabbitPageSpeedInfo entity = new RabbitPageSpeedInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // pageName
            cursor.getLong(offset + 2), // time
            cursor.getLong(offset + 3), // createStartTime
            cursor.getLong(offset + 4), // createEndTime
            cursor.getLong(offset + 5), // inflateFinishTime
            cursor.getLong(offset + 6), // fullDrawFinishTime
            cursor.getLong(offset + 7), // resumeEndTime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // apiRequestCostString
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RabbitPageSpeedInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPageName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTime(cursor.getLong(offset + 2));
        entity.setCreateStartTime(cursor.getLong(offset + 3));
        entity.setCreateEndTime(cursor.getLong(offset + 4));
        entity.setInflateFinishTime(cursor.getLong(offset + 5));
        entity.setFullDrawFinishTime(cursor.getLong(offset + 6));
        entity.setResumeEndTime(cursor.getLong(offset + 7));
        entity.setApiRequestCostString(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RabbitPageSpeedInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RabbitPageSpeedInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RabbitPageSpeedInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
