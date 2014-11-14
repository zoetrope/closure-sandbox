import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class SimpleFuture<T> implements Future<T> {

    private Supplier<T> supplier;
    public SimpleFuture(Supplier<T> supplier){
        this.supplier = supplier;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return supplier.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return supplier.get();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
