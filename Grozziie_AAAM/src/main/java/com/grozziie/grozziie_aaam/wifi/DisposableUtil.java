package com.grozziie.grozziie_aaam.wifi;
import io.reactivex.disposables.Disposable;

public class DisposableUtil {

    public static void dispose(Disposable disposable) {

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

    public static boolean isValid(Disposable disposable) {
        return null != disposable && !disposable.isDisposed();
    }
}
