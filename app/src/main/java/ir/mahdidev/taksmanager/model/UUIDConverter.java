package ir.mahdidev.taksmanager.model;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.UUID;

public class UUIDConverter implements PropertyConverter<UUID, String> {
    @Override
    public UUID convertToEntityProperty(String databaseValue) {
        UUID myUUID = UUID.fromString(databaseValue);
        if (myUUID.toString().equals(databaseValue)){
            return myUUID ;
        }
        return null;
    }

    @Override
    public String convertToDatabaseValue(UUID entityProperty) {
        return entityProperty.toString();
    }
}
