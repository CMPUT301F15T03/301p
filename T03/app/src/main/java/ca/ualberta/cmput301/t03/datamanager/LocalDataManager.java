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
 * A {@link JsonDataManager} that stores the serialized JSONs locally in the phone's storage.
 * Created by rishi on 15-10-30.
 */
public class LocalDataManager extends JsonDataManager {

    private final Context context;

    /**
     * Creates an instance of the {@link LocalDataManager}.
     * @param context The {@link Context} to be used for accessing the filesystem.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public LocalDataManager(Context context, boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
        this.context = Preconditions.checkNotNull(context, "context");
    }

    /**
     * Creates an instance of the {@link LocalDataManager}. The "useExplicitExposeAnnotation"
     * value is set to false.
     * @param context The {@link Context} to be used for accessing the filesystem.
     */
    public LocalDataManager(Context context) {
        this(context, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyExists(DataKey key) {
        Preconditions.checkNotNull(key, "key");
        return getTargetFile(key, false).exists();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteIfExists(DataKey key) {
        if (!keyExists(key)) {
            return false;
        }

        File targetFile = getTargetFile(key, false);
        targetFile.delete();
        return true;
    }

    /**
     * Not applicable for LocalDataManager. Always returns true.
     * @return Always true.
     */
    @Override
    public boolean isOperational() {
        return true;
    }

    // Source: http://stackoverflow.com/questions/2130932/how-to-create-directory-automatically-on-sd-card
    private File getTargetFile(DataKey key, boolean createTypeDirectoryIfNotExists) {
        File appContextRootDirectory = context.getFilesDir();
        File typeDirectory = new File(appContextRootDirectory, key.getType());
        if (createTypeDirectoryIfNotExists && !typeDirectory.exists()) {
            typeDirectory.mkdirs();
        }
        return new File(typeDirectory, key.getId() + ".json");
    }
}
