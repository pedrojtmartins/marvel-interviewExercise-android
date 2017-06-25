package pedrojtmartins.com.farfetchmarvel.interfaces;

/**
 * Pedro Martins
 * 22/06/2017
 */

public interface IItemInteraction<T> {
    void onItemClick(T item);
    void onItemLongClick(T item);
}
