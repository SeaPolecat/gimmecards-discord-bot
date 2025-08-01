package ca.gimmecards.consts;
import java.util.Map;

public class SetCodeConsts {

    // the Pokemon TCG Online codes for each set; used to determine which cards to crawl from the API
    
    public static final Map<Integer, String> SET_CODES = Map.ofEntries(
        Map.entry(1, "BLW"),
        Map.entry(2, "EPO"),
        Map.entry(3, "NVI"),
        Map.entry(4, "NXD"),
        Map.entry(5, "DEX"),
        Map.entry(6, "DRX"),
        Map.entry(7, "BCR"),
        Map.entry(8, "PLS"),
        Map.entry(9, "PLF"),
        Map.entry(10, "PLB"),
        Map.entry(11, "LTR"),
        Map.entry(12, "XY"),
        Map.entry(13, "FLF"),
        Map.entry(14, "FFI"),
        Map.entry(15, "PHF"),
        Map.entry(16, "PRC"),
        Map.entry(17, "ROS"),
        Map.entry(18, "AOR"),
        Map.entry(19, "BKT"),
        Map.entry(20, "BKP"),
        Map.entry(21, "FCO"),
        Map.entry(22, "STS"),
        Map.entry(23, "EVO"),
        Map.entry(24, "SUM"),
        Map.entry(25, "GRI"),
        Map.entry(26, "BUS"),
        Map.entry(27, "CIN"),
        Map.entry(28, "UPR"),
        Map.entry(29, "FLI"),
        Map.entry(30, "CES"),
        Map.entry(31, "LOT"),
        Map.entry(32, "TEU"),
        Map.entry(33, "UNB"),
        Map.entry(34, "UNM"),
        Map.entry(35, "CEC"),
        Map.entry(36, "SSH"),
        Map.entry(37, "RCL"),
        Map.entry(38, "DAA"),
        Map.entry(39, "VIV"),
        Map.entry(40, "BST"),
        Map.entry(41, "CRE"),
        Map.entry(42, "EVS"),
        Map.entry(43, "FST"),
        Map.entry(44, "BRS"),
        Map.entry(45, "ASR"),
        Map.entry(46, "LOR"),
        Map.entry(47, "SIT"),
        Map.entry(48, "sv1"), // SVI
        Map.entry(49, "sv2"), // PAL
        Map.entry(50, "sv3"), // OBF
        Map.entry(51, "sv4"), // PAR
        Map.entry(52, "sv5"), // TEF
        Map.entry(53, "sv6"), // TWM
        Map.entry(54, "sv7"), // SCR
        Map.entry(55, "sv8") // SSP
    );

    public static final Map<Integer, String> OLD_SET_CODES = Map.ofEntries(
        Map.entry(1, "BS"),
        Map.entry(2, "JU"),
        Map.entry(3, "FO"),
        Map.entry(4, "B2"),
        Map.entry(5, "TR"),
        Map.entry(6, "G1"),
        Map.entry(7, "G2"),
        Map.entry(8, "N1"),
        Map.entry(9, "N2"),
        Map.entry(10, "N3"),
        Map.entry(11, "N4"),
        Map.entry(12, "LC"),
        Map.entry(13, "EX"),
        Map.entry(14, "AQ"),
        Map.entry(15, "SK"),
        Map.entry(16, "RS"),
        Map.entry(17, "SS"),
        Map.entry(18, "DR"),
        Map.entry(19, "MA"),
        Map.entry(20, "HL"),
        Map.entry(21, "TRR"),
        Map.entry(22, "DX"),
        Map.entry(23, "EM"),
        Map.entry(24, "UF"),
        Map.entry(25, "DS"),
        Map.entry(26, "LM"),
        Map.entry(27, "HP"),
        Map.entry(28, "CG"),
        Map.entry(29, "DF"),
        Map.entry(30, "PK"),
        Map.entry(31, "DP"),
        Map.entry(32, "MT"),
        Map.entry(33, "SW"),
        Map.entry(34, "GE"),
        Map.entry(35, "MD"),
        Map.entry(36, "LA"),
        Map.entry(37, "SF"),
        Map.entry(38, "PL"),
        Map.entry(39, "RR"),
        Map.entry(40, "SV"),
        Map.entry(41, "AR"),
        Map.entry(42, "HS"),
        Map.entry(43, "UL"),
        Map.entry(44, "UD"),
        Map.entry(45, "TM"),
        Map.entry(46, "CL")
    );

    public static final Map<Integer, String> RARE_SET_CODES = Map.ofEntries(
        Map.entry(1, "DRV"),
        Map.entry(2, "DCR"),
        Map.entry(3, "GEN"),
        Map.entry(4, "SLG"),
        Map.entry(5, "DRM"),
        Map.entry(6, "HIF"),
        Map.entry(7, "CPA"),
        Map.entry(8, "SHF"),
        Map.entry(9, "CEL")
    );

    public static final Map<Integer, String> PROMO_SET_CODES = Map.ofEntries(
        Map.entry(1, "NP"),
        Map.entry(2, "DPP"),
        Map.entry(3, "HS"),
        Map.entry(4, "BLW"),
        Map.entry(5, "XY"),
        Map.entry(6, "SW")
    );
}
