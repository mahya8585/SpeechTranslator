package sttApi.storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Azure Storage接続処理
 * Created by maaya on 2017/12/27.
 */
public class Access {
    //接続文字列
    //TODO application.yamlに移す
    private static final String ACCOUNT_NAME = "Azure Storageアカウント名を入れてください";
    private static final String ACCOUNT_KEY = "Azure StorageアカウントKEYを入れてください";
    private static final String STORAGE_CONNECTION_STRING =
            "DefaultEndpointsProtocol=http;" + "AccountName=" + ACCOUNT_NAME + ";" + "AccountKey=" + ACCOUNT_KEY;


    /**
     * Storage共通処理。コンテナー情報の取得
     *
     * @param containerName コンテナ名
     * @return
     * @throws URISyntaxException
     * @throws StorageException
     * @throws InvalidKeyException
     */
    CloudBlobContainer createCloudBlobContainer(String containerName) throws URISyntaxException, StorageException, InvalidKeyException {
        // 接続
        CloudStorageAccount storageAccount = createStorageAccount();
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        //コンテナリファレンスの取得
        return blobClient.getContainerReference(containerName);
    }

    /**
     * 接続先設定
     *
     * @return 接続先設定
     * @throws URISyntaxException
     * @throws InvalidKeyException
     */
    private CloudStorageAccount createStorageAccount() throws URISyntaxException, InvalidKeyException {
        return CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
    }

}
