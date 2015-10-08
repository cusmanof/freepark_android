package au.com.fc.dialogs;

/**
 * @author Frank Cusmano
 */
public interface IDialog {

    /**
     * the user has selected an item.
     * @param sel the selection.
     */
    public void aSelection(String sel);

    public void aIndex(int sel);
    /**
     * the user has pressed yes.
     */
    public void aYes();

    /**
     * the user has pressed no.
     */
    public void  aNo();
}
