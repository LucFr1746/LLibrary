package io.github.lucfr1746.llibrary.requirement;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RequirementLoader {

    public List<Requirement> getRequirements(@Nullable ConfigurationSection checkingSection) {
        List<Requirement> requirements = new ArrayList<>();
        if (checkingSection == null) return requirements;

        for (String key : checkingSection.getKeys(false)) {
            ConfigurationSection requirementSection = checkingSection.getConfigurationSection(key);
            if (requirementSection == null) {
                LLibrary.getLoggerAPI().error("Failed to load requirement section: " + key + ". Skipping...");
                continue;
            }

            String typeString = requirementSection.getString("type", "").toUpperCase();
            RequirementType requirementType;
            try {
                requirementType = RequirementType.valueOf(typeString);
            } catch (IllegalArgumentException exception) {
                LLibrary.getLoggerAPI().error("Invalid or missing requirement type: " + typeString + " in section: " + key + ". Skipping...");
                continue;
            }

            List<Action> denyActions = new ActionLoader().getActions(requirementSection.getStringList("deny-action"));

            switch (requirementType) {
                case PERMISSION -> {
                    String permission = requirementSection.getString("permission");
                    if (permission == null || permission.isBlank()) {
                        LLibrary.getLoggerAPI().error("Missing 'permission' value in section: " + key + ". Skipping...");
                        continue;
                    }
                    requirements.add(new HasPermissionRequirement(permission).setDenyHandler(denyActions));
                }
                case EXP, LEVEL -> {
                    String amountString = requirementSection.getString("amount", "");
                    try {
                        int amount = Integer.parseInt(amountString);
                        Requirement requirement = (requirementType == RequirementType.EXP)
                                ? new HasExpRequirement(amount)
                                : new HasLevelRequirement(amount);
                        requirements.add(requirement.setDenyHandler(denyActions));
                    } catch (NumberFormatException exception) {
                        LLibrary.getLoggerAPI().error("Invalid 'amount' value: " + amountString + " in section: " + key + ". Skipping...");
                    }
                }
            }
        }

        return requirements;
    }
}
