package be.jocls.domain.repository;

import be.jocls.domain.model.Item;
import be.jocls.domain.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemType(ItemType itemType);
}
