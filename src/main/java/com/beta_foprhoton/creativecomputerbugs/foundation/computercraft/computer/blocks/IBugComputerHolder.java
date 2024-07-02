package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.computer.blocks;

import dan200.computercraft.shared.computer.core.ComputerFamily;

import javax.annotation.Nullable;

public interface IBugComputerHolder {
    int getComputerID();

    void setComputerID(int id);

    @Nullable
    String getLabel();

    void setLabel(@Nullable String label);

    ComputerFamily getFamily();
}
