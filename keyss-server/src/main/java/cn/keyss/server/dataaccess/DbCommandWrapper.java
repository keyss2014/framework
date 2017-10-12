package cn.keyss.server.dataaccess;

import cn.keyss.server.data.ShardType;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * 指令包装器
 *
 */
class DbCommandWrapper implements Cloneable, Serializable {

    //region private fields
    private static final long serialVersionUID = 5796945599860584839L;
    private ShardType shardType;
    private Object command;
    private String commandType;
    //endregion

    public ShardType getShardType() {
        return shardType;
    }

    public void setShardType(ShardType shardType) {
        this.shardType = shardType;
    }

    public Object getCommand() {
        return command;
    }

    public void setCommand(Object command) {
        this.command = command;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    @Override
    public DbCommandWrapper clone() {
        Object cloneObject = SerializationUtils.deserialize(SerializationUtils.serialize(this));
        return this.getClass().cast(cloneObject);
    }
}
