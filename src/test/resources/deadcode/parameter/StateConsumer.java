package deadcode.parameter;

import com.aurea.model.DeadCodeDetection;

public interface StateConsumer {
  void accept(DeadCodeDetection deadCodeDetection);
}
