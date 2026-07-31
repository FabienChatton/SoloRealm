package ch.solorealm.beans.levels;

import ch.solorealm.beans.machine.MachineNode;

import java.util.function.Supplier;

public record ShopNode(Supplier<MachineNode> machineNodeSupplier, int shopLevel) { }
