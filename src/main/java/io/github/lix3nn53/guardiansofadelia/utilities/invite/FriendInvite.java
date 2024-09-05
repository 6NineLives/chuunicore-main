package io.github.lix3nn53.guardiansofadelia.utilities.invite;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.entity.Player;

public class FriendInvite extends Invite {

    public FriendInvite(Player sender, Player receiver, String senderTitle, String receiverMessage, String receiverTitle) {
        super(sender, receiver, senderTitle, receiverMessage, receiverTitle);
    }

    @Override
    public void accept() {
        Player sender = getSender();
        Player receiver = getReceiver();

        if (GuardianDataManager.hasGuardianData(sender) && GuardianDataManager.hasGuardianData(receiver)) {
            GuardianData senderData = GuardianDataManager.getGuardianData(sender);
            GuardianData receiverData = GuardianDataManager.getGuardianData(receiver);

            senderData.addFriend(receiver);
            receiverData.addFriend(sender);

            sender.sendMessage(ChatPalette.BLUE_LIGHT + receiver.getName() + " accepted your friend request");
            receiver.sendMessage(ChatPalette.BLUE_LIGHT + "You are now friends with " + sender.getName());
        }

        super.accept();
    }

    @Override
    public void reject() {
        getSender().sendMessage(ChatPalette.RED + getReceiver().getName() + " rejected your trade invite");
        super.reject();
    }
}
