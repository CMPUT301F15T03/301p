package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Scanner;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;

/**
 * Created by rishi on 15-10-30.
 */
public class LocalDataManager extends JsonDataManager {

    private final Context context;

    public LocalDataManager(Context context, boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
        this.context = Preconditions.checkNotNull(context, "context");
    }

    public LocalDataManager(Context context) {
        this(context, false);
    }

    @Override
    public boolean keyExists(DataKey key) {
        Preconditions.checkNotNull(key, "key");
        return getTargetFile(key, false).exists();
    }

    @Override
    public <T> T getData(DataKey key, Type typeOfT) {
        Preconditions.checkNotNull(key, "key");

        if (!keyExists(key)) {
            throw new DataKeyNotFoundException(key.toString());
        }

        File targetFile = getTargetFile(key, false);

        Scanner scanner;
        try {
            scanner = new Scanner(targetFile);
        } catch (FileNotFoundException e) {
            throw new NotImplementedException("Dev note: This exception shouldn't have been thrown, as the necessary dirs are created.", e);
        }

        String fileContents = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return deserialize(fileContents, typeOfT);
    }

    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) {
        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(obj, "obj");

        String json = serialize(obj, typeOfT);
        File targetFile = getTargetFile(key, true);
        PrintWriter out;

        try {
            out = new PrintWriter(new FileOutputStream(targetFile, false));
        } catch (FileNotFoundException e) {
            throw new NotImplementedException("Dev note: This exception shouldn't have been thrown, as the necessary dirs are created.", e);
        }

        out.print(json);
        out.close();
    }

    @Override
    public boolean deleteIfExists(DataKey key) {
        if (!keyExists(key)) {
            return false;
        }

        File targetFile = getTargetFile(key, false);
        targetFile.delete();
        return true;
    }

    @Override
    public boolean isOperational() {
        return true;
    }

    // Source: http://stackoverflow.com/questions/2130932/how-to-create-directory-automatically-on-sd-card
    private File getTargetFile(DataKey key, boolean createTypeDirectoryIfNotExists) {
        File appContextRootDirectory = context.getFilesDir();
        File typeDirectory = new File(appContextRootDirectory, key.getType());
        if (createTypeDirectoryIfNotExists && !typeDirectory.exists()) {
            typeDirectory.mkdir();
        }
        return new File(typeDirectory, key.getId() + ".json");
    }
}
