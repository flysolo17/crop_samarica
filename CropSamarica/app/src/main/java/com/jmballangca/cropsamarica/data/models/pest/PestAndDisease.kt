package com.jmballangca.cropsamarica.data.models.pest
import android.util.Log
import com.jmballangca.cropsamarica.BuildConfig
import com.jmballangca.cropsamarica.core.common.LocaleManager
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import java.util.Locale




fun LocalizeText.locale(
    languageCode : String
): String {
    return if (languageCode == "en") {
        this.en
    } else {
        this.tl
    }
}
data class LocalizeText(
    val en : String = "",
    val tl : String = ""
)
fun String.localize() : LocalizeText {
    return LocalizeText(en = this, tl = this)
}

fun List<String>.localize() : List<LocalizeText> {
    return this.map { LocalizeText(en = it, tl = it) }
}

data class PestAndDisease(
    val id : String = "",
    val images: List<String> = emptyList(),
    val stages : List<RiceStage> = emptyList(),
    val title : LocalizeText = LocalizeText(),
    val description : LocalizeText = LocalizeText(),
    val symptoms : List<LocalizeText> = emptyList(),
    val prevention : List<Prevention> = emptyList(),
) {
    companion object{

        val ALL = listOf(
            GOLDEN_APPLE_SNAIL,
            BACTERIAL_LEAF_BLIGHT,
            CASEWORM,
            RICE_BLAST,
            RODENTS,
            STEM_BORER,
            BROWN_SPOT,
            LEAF_FOLDER,
            BROWN_PLANTHOPPER,
            GREEN_LEAFHOPPER,
            FALSE_SMUT,
            SHEATH_ROT,
            SHEATH_BLIGHT,
            BIRDS,
            RICE_BUG,
            RICE_BLACK_BUG
        )
    }
}
data class Prevention(
    val title : LocalizeText = LocalizeText(
        en = "",
        tl = ""
    ),
    val text : List<LocalizeText> = emptyList(),
)

val  GOLDEN_APPLE_SNAIL = PestAndDisease(
    id = "1",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Golden%20apple%20snail-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Golden%20apple%20snail.jfif"
    ),
    title = LocalizeText(
        en = "Golden Apple Snail",
        tl = "Golden Apple Snail"
    ),
    description =
        LocalizeText(
            tl = "",
            en = "Golden apple snails eat young and emerging rice plants. They cut the rice stem at the base, destroying the whole plant. Snails are able to spread through irrigation canals, natural water distribution pathways, and during flooding events. They damage direct wet-seeded rice and transplanted rice up to 30 days old. Once the rice plant reaches 30−40 days, it will become thick enough to resist the snail."
        ),
    symptoms = listOf(
        LocalizeText(
            en = "Missing rice hills",
            tl = "Cut rice stems at the base"
        ),
        LocalizeText(
            en = "Damaged or cut leaves",
            tl = ""
        ),
        LocalizeText(
            en = "Presence of bright pink egg masses",
            tl = ""
        ),
        LocalizeText(
            en = "Destruction of whole rice seedlings, especially under 30 days old",
            tl = ""
        )
    ),
    prevention = listOf(
        Prevention(
            title = LocalizeText(
                en= "Cultural and Physical Controls"
            ),
            text = listOf(
                "Handpick snails and crush eggs (best done morning/afternoon)",
                "Place bamboo stakes for egg collection",
                "Use attractant plants (papaya, cassava leaves) to lure snails",
                "Keep water level below 2 cm during early crop stages",
                "Build canalettes to drain water and trap snails",
                "Block field entry with screens or mesh at water inlets/outlets",
                "Transplant 25–30 day-old seedlings from low-density nurseries",
                "Plant multiple seedlings per hill",
            ).localize()
        ),
        Prevention(
            title = "Biological Control".localize(),
            text = listOf(
                "Encourage ducks, red ants, rats, and birds to eat snails or eggs",
                "Let ducks forage in the field after 30–35 days",
                "Harvest and cook snails for food or animal feed (with care due to health risk"
            ).localize()
        ),
        Prevention(
            title = "Use of Toxic Plants".localize(),
            text = listOf(
                "Place tobacco leaves, citrus leaves, or heartleaf false pickerelweed across fields"
            ).localize()
        ),
        Prevention(
            title = "Chemical Control (if necessary)".localize(),
            text = listOf(
                "Use molluscicides only during seedling phase (<30 days old)",
                "Apply in canalettes or low spots only",
                "Follow safe fertilizer schedule (in 2 cm water) to affect snails",
                "Choose chemicals with low human/environmental toxicity"
            ).localize()
        )
    )
)


// 2. Bacterial Leaf Blight
val BACTERIAL_LEAF_BLIGHT = PestAndDisease(
    id = "2",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Bacterial%20Leaf%20Blight-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Bacterial%20Leaf%20Blight.jpg"
    ),
    title = "Bacterial Leaf Blight – Fungus".localize(),
    description = "Bacterial leaf blight of rice is caused by the bacterium Xanthomonas oryzae pv. oryzae, which can survive on grass weeds and stubbles of infected plants. The disease spreads through wind, rain splash, and irrigation water, and its incidence becomes more severe under frequent rainfall, strong winds, humidity above 70%, and temperatures ranging from 25°C to 34°C. Practices such as heavy nitrogen fertilization and close planting favor its development. The earlier the infection, the higher the yield loss.".localize(),
    symptoms = listOf(
        "Seedlings: grey-green streaks from tips/margins → turn yellow-white with wavy edges → wilt and die",
        "Transplanted rice (early infection): whole plant wilts and dies (called kresek)",
        "Older plants: pale yellow streaks with wavy edges → dry and die",
        "Later infection (booting stage): poor grain quality but not yield",
        "Signs: milky droplets oozing from streaks that dry as crust"
    ).localize(),
    prevention = listOf(
        Prevention(
            title = "Biosecurity".localize(),
            text = listOf(
                "Use certified BLB-free seed",
                "Quarantine and test imported rice seeds"
            ).localize()
        ),
        Prevention(
            title = "Cultural Control".localize(),
            text = listOf(
                "Before planting: select seed only from healthy, BLB-free plants, plant resistant rice varieties",
                "During growth: avoid injuring seedlings when transplanting, avoid excessive nitrogen (80–100 kg/ha), maintain good drainage, keep fields weed-free (esp. Leersia)",
                "After harvest: plough rice stubble, straw, and ratoons under soil, allow fields to dry (fallow) to kill bacteria"
            ).localize()
        )
    )
)

// 3. Caseworm
val CASEWORM = PestAndDisease(
    id = "3",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Caseworm-2.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Caseworm.jpg"
    ),
    title = "Caseworm – Pest".localize(),
    description = "The rice caseworm is commonly found in rice fields with standing water, whether in wetland or irrigated environments. It survives on weeds and weedy rice nearby and infests new rice crops under favorable conditions. The pest is more problematic when young seedlings are transplanted. Poor land preparation and zinc-deficient soils predispose rice to damage. Outbreaks may cause significant damage if not managed properly.".localize(),
    symptoms = listOf(
        "Young larvae graze on leaf surfaces, leaving papery remains",
        "Cut rice leaf tips at right angles, forming floating leaf cases",
        "Leaves show ladder-like structures of hard fibers after feeding",
        "Confirmed by leaf cases attached to sheaths or floating"
    ).localize(),
    prevention = listOf(
        Prevention(
            title = "Preventive Measure".localize(),
            text = listOf(
                "Plant early to reduce incidence",
                "Use wider spacing (30 × 20 cm)",
                "Transplant older seedlings and destroy remaining eggs",
                "Drain fields and use filters when re-irrigating after 2–3 days",
                "Apply fertilizers properly (avoid excess nitrogen; ensure potassium)",
                "Remove weeds and weedy rice in and around fields"
            ).localize()
        ),
        Prevention(
            title = "Organic Control".localize(),
            text = listOf(
                "Encourage natural enemies: snails (eat eggs), water beetles (larvae), spiders, dragonflies, birds (adults)",
                "Apply ash or neem leaf extract where insects are found"
            ).localize()
        ),
        Prevention(
            title = "Chemical Control".localize(),
            text = listOf(
                "Use integrated management with preventive and biological methods",
                "Apply authorized carbamate insecticides if needed",
                "Avoid pyrethroids due to tolerance"
            ).localize()
        )
    )
)

// 4. Rice Blast
val RICE_BLAST = PestAndDisease(
    id = "4",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rice%20blast-2.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rice%20blast.jpg"
    ),
    title = "Rice Blast – Fungus".localize(),
    description = "Caused by Magnaporthe oryzae. One of the most destructive diseases. Infects at nearly all stages. Promoted by excess nitrogen, cool/moist weather, drought stress, and poor drainage.".localize(),
    symptoms = listOf(
        "Yellow/green eye-shaped lesions with necrotic borders",
        "Lesions enlarge → leaf drying and collar rot",
        "Infected nodes brown → stem breakage, plant death",
        "Reduced leaf area, grain fill, yield"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Organic Control".localize(),
            listOf("No fully effective biological control (research ongoing)").localize()
        ),
        Prevention(
            "Chemical Control".localize(),
            listOf(
                "Use seed treatment with thiram",
                "Apply fungicides (azoxystrobin, triazoles, strobilurins) in nursery/tillering/panicle stage",
                "1–2 sprays at heading stage reduce infection"
            ).localize()
        )
    )
)

// 5. Rodents
val RODENTS = PestAndDisease(
    id = "5",
    stages = listOf(
        RiceStage.SEEDLING,
        RiceStage.TILLERING,
        RiceStage.BOOTING,
        RiceStage.MILKING,
        RiceStage.MATURE
    ),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rodents-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rodents.jpg"
    ),
    title = "Rodents – Pest".localize(),
    description = "Rice field rats (Rattus argentiventer) feed at all stages. Populations grow fast (up to 1,500/year). Thrive in double/triple cropping, weedy bunds, unmanaged edges, and burrows.".localize(),
    symptoms = listOf(
        "Seeds: missing/chewed",
        "Seedlings: chopped/missing hills",
        "Tillering: clean cuts",
        "Booting: damaged panicles",
        "Ripening: cut/eaten grains, delayed maturity"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Community Control".localize(),
            listOf(
                "Synchronize planting",
                "Clean bunds/edges",
                "Use barriers (plastic fences, traps)",
                "Community rat campaigns",
                "Use traps/fumigation"
            ).localize()
        )
    )
)

// 6. Stem Borer
val STEM_BORER = PestAndDisease(
    id = "6",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.STEM_ELONGATION, RiceStage.BOOTING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Stem%20Borer-2.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Stem%20Borer.jpg"
    ),
    title = "Stem Borer – Pest".localize(),
    description = "Stem borer infestation is caused by larvae of moth species such as the yellow stem borer (Scirpophaga incertulas) and the white stem borer (Scirpophaga innotata). The larvae bore into stems, feeding inside and cutting the flow of water and nutrients. They survive in rice stubbles, straw piles, ratoons, and volunteer rice, which serve as breeding and carry-over hosts. Continuous rice planting and poor residue management favor their rapid spread.".localize(),
    symptoms = listOf(
        "Deadheart: central shoot dies during the vegetative stage",
        "Whitehead: empty, whitish panicles during the reproductive stage",
        "Presence of egg masses on rice leaves",
        "Cut stems with visible larval tunnels inside"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Plow or rotavate the field immediately after harvest to destroy stubbles and residues that harbor larvae and pupae",
                "Practice synchronous planting within the community to break pest cycles",
                "Use resistant or tolerant varieties when available",
                "Apply fertilizer properly to maintain crop vigor",
                "Monitor fields regularly for egg masses and damage"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf(
                "Conserve natural enemies such as egg parasitoids (Trichogramma)",
                "Encourage predators like spiders, mirid bugs, and ants"
            ).localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf(
                "Apply insecticides only when the economic threshold is reached (1–2 egg masses per m²)",
                "Avoid spraying when whiteheads are already visible, as damage has already occurred"
            ).localize()
        )
    )
)


// 7. Brown Spot
val BROWN_SPOT = PestAndDisease(
    id = "7",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Brown%20spot-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Brown%20spot.jpg"
    ),
    title = "Brown Spot – Fungus".localize(),
    description = "Brown Spot is caused by the fungus Bipolaris oryzae. It attacks rice from seedling to maturity, especially under nutrient-deficient and drought-stressed conditions. The fungus survives in infected seeds, plant residues, and soil. Continuous planting, poor soil fertility, and unfavorable field management practices favor its spread.".localize(),
    symptoms = listOf(
        "Small circular to oval brown lesions on leaves",
        "Lesions enlarge with yellow halo or reddish-brown margin",
        "Seedling blight in nurseries, causing drying of leaves",
        "Reduced tillering and poor grain filling",
        "Sometimes empty grains"
    ).localize(),

    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Use clean and certified seeds",
                "Apply balanced fertilization with sufficient potassium and silicon",
                "Avoid excessive nitrogen application",
                "Improve field drainage and avoid prolonged drought",
                "Practice crop rotation",
                "Remove infected crop residues after harvest"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Encourage beneficial soil microbes to suppress fungal growth").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf("Apply fungicides only when infection is severe and yield loss is expected").localize()
        )
    )
)

// 8. Leaf Folder
val LEAF_FOLDER = PestAndDisease(
    id = "8",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Leaf%20Folder-2.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Leaf%20Folder.jpg"
    ),
    title = "Leaf Folder – Pest".localize(),
    description = "Leaf Folder infestation is caused by larvae of moths (Cnaphalocrocis medinalis and related species). The larvae hide and feed inside folded leaves, reducing photosynthesis. Infestation increases with continuous planting, dense canopy, and high nitrogen use. Adults are mobile and spread quickly across fields.".localize(),
    symptoms = listOf(
        "Leaves folded lengthwise and fastened with silk threads",
        "Feeding damage appears as white streaks or transparent areas",
        "Severe infestation causes leaf drying and reduced photosynthesis",
        "Presence of greenish caterpillars inside folded leaves"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Avoid excessive nitrogen fertilizer",
                "Practice synchronous planting",
                "Monitor fields regularly for folded leaves and larvae",
                "Use resistant or tolerant varieties when available"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf(
                "Conserve natural enemies like Trichogramma parasitoids",
                "Encourage predators such as spiders and mirid bugs"
            ).localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf(
                "Apply insecticides only when leaf damage exceeds 10% at maximum tillering stage",
                "Use recommended insecticides carefully to avoid harming natural enemies"
            ).localize()
        )
    )
)

// 9. Brown Planthopper
val BROWN_PLANTHOPPER = PestAndDisease(
    id = "9",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Brown%20Planthopper-2.png",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Brown%20Planthopper.jpg"
    ),
    title = "Brown Planthopper – Pest".localize(),
    description = "Brown planthoppers (Nilaparvata lugens) suck plant sap from the base of rice stems, weakening plants and reducing yield. Heavy infestations cause hopperburn, destroying entire fields. They also transmit grassy stunt and ragged stunt viruses. Populations increase rapidly in dense, nitrogen-rich fields and continuous cropping systems.".localize(),
    symptoms = listOf(
        "Yellowing and wilting of rice plants from the base",
        "Hopperburn – patches of dead plants due to heavy feeding",
        "Stunted growth and poor tillering",
        "Presence of brown, small, winged insects at the base of rice plants"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Avoid excessive nitrogen application",
                "Plant resistant or tolerant rice varieties",
                "Practice synchronous planting and maintain a fallow period between crops",
                "Drain fields for a few days when populations are high"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf(
                "Conserve natural enemies like mirid bugs, spiders, and parasitoids"
            ).localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf(
                "Apply selective insecticides only when populations exceed thresholds (10 insects per hill early stage, 25–30 per hill later stage)",
                "Avoid unnecessary spraying to preserve natural enemies"
            ).localize()
        )
    )
)



// 10. Green Leafhopper
val GREEN_LEAFHOPPER = PestAndDisease(
    id = "10",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Green%20Leafhopper%20-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Green%20Leafhopper.jfif"
    ),
    title = "Green Leafhopper – Pest".localize(),
    description = "Green leafhoppers (Nephotettix virescens and related species) feed on rice by sucking plant sap, weakening plants and reducing yield. More importantly, they transmit tungro virus, one of the most destructive rice diseases. Populations increase rapidly with continuous planting, excessive nitrogen, and abundant weeds that serve as hosts.".localize(),
    symptoms = listOf(
        "Yellowing and stunting of rice plants",
        "Hopperburn-like patches under heavy infestation",
        "Presence of small green wedge-shaped insects on leaves and stems",
        "Transmission of tungro virus causing stunted plants with yellow/orange leaves"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Use resistant or tungro-tolerant rice varieties",
                "Practice synchronous planting to reduce continuous food sources",
                "Control weeds in and around rice fields",
                "Avoid excessive nitrogen fertilizer"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Encourage predators such as spiders, mirid bugs, and egg parasitoids").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf(
                "Use insecticides only when populations are high and tungro risk is present",
                "Apply products carefully to protect natural enemies"
            ).localize()
        )
    )
)

// 11. False Smut
val FALSE_SMUT = PestAndDisease(
    id = "11",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/False%20Smut.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/False%20Smut.jpg"
    ),
    title = "False Smut – Fungus".localize(),
    description = "False smut is caused by Ustilaginoidea virens. It affects rice from booting to maturity. The pathogen survives in soil and infected grains, spreading through air, rain, and irrigation water. High humidity and excess nitrogen favor its development.".localize(),
    symptoms = listOf(
        "Yellowish-green spore balls on spikelets",
        "Swollen grains replaced by velvety green fungal masses",
        "Infected grains fail to develop, reducing quality",
        "Heavily infected panicles appear discolored"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Plant resistant varieties and use certified seeds",
                "Avoid excessive nitrogen, ensure balanced fertilization",
                "Practice crop rotation and maintain field sanitation"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Apply biocontrol agents such as Trichoderma spp. in soil").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf("Spray fungicides during booting and heading stages").localize()
        )
    )
)

// 12. Sheath Rot
val SHEATH_ROT = PestAndDisease(
    id = "12",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/sheath%20rot-2.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/sheath%20rot.jpg"
    ),
    title = "Sheath Rot – Fungus".localize(),
    description = "Sheath rot is mainly caused by Sarocladium oryzae. Infection occurs at booting to heading stages, favored by high humidity, dense planting, and excess nitrogen. The pathogen survives in seeds and crop residues and spreads through wind and rain.".localize(),
    symptoms = listOf(
        "Irregular brown lesions on leaf sheath enclosing the panicle",
        "Rotting panicles with unfilled or discolored grains",
        "Panicles fail to emerge properly (choked panicle)",
        "Grain discoloration and yield loss"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Use clean seeds and resistant varieties",
                "Avoid excessive nitrogen",
                "Rotate crops and remove infected residues"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Promote beneficial microbes that compete with pathogens").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf("Apply fungicides at booting stage when disease pressure is high").localize()
        )
    )
)


// 13. Sheath Blight
val SHEATH_BLIGHT = PestAndDisease(
    id = "13",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Sheath%20blight-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Sheath%20blight.jpg"

    ),
    title = "Sheath Blight – Fungus".localize(),
    description = "Sheath blight is caused by Rhizoctonia solani. It spreads rapidly in fields with high humidity, dense planting, and high nitrogen input. The fungus survives in soil and plant debris, infecting rice from tillering to ripening stages.".localize(),
    symptoms = listOf(
        "Oval or irregular lesions on leaf sheaths near the waterline",
        "Grayish-green lesions with brown margins that enlarge and join",
        "Infected tillers weaken and lodge",
        "Severe cases lead to significant yield loss"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Avoid dense planting and excessive nitrogen",
                "Improve drainage and manage water efficiently",
                "Remove infected plant debris after harvest"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Use antagonistic fungi such as Trichoderma spp.").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf("Apply fungicides when initial symptoms appear").localize()
        )
    )
)

// 14. Birds
val BIRDS = PestAndDisease(
    id = "14",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Bird-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Bird.jpg"
    ),
    title = "Birds – Pest".localize(),
    description = "Birds such as maya (Lonchura spp.) and other grain-feeding species damage rice mainly during the ripening stage. They peck and eat grains, causing severe yield loss in small fields near roosting or nesting areas.".localize(),
    symptoms = listOf(
        "Missing grains on panicles",
        "Broken or cut panicles due to feeding",
        "Patches of rice fields with low yield",
        "Flocks of birds feeding during ripening stage"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Practice synchronous planting with neighbors",
                "Use bird scarers (flags, ribbons, noise makers)",
                "Guard fields during ripening",
                "Harvest promptly to reduce bird feeding time"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Encourage natural predators such as raptors and owls").localize()
        )
    )
)

// 15. Rice Bug
val RICE_BUG = PestAndDisease(
    id = "15",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rice%20bug-2.jpg",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rice%20bug.png"
    ),
    title = "Rice Bug – Pest".localize(),
    description = "Rice bugs (Leptocorisa spp.) feed on rice grains during the milking to ripening stage by sucking the contents of developing kernels. This reduces grain quality and yield. They emit a foul odor when disturbed and populations build up in continuous cropping areas.".localize(),
    symptoms = listOf(
        "Empty or unfilled grains",
        "Discolored grains with black spots",
        "Distorted and shriveled kernels",
        "Foul odor when bugs are crushed"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Plant rice synchronously",
                "Control weeds around fields",
                "Handpick bugs early in the morning when less active"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Conserve egg parasitoids and predators such as spiders and assassin bugs").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf("Apply insecticides only during milking to early ripening stage if density is high").localize()
        )
    )
)

// 16. Rice Black Bug
val RICE_BLACK_BUG = PestAndDisease(
    id = "16",
    stages = listOf(RiceStage.SEEDLING, RiceStage.TILLERING, RiceStage.BOOTING, RiceStage.FLOWERING),
    images = listOf(
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rice%20black%20bug.jfif",
        "https://raw.githubusercontent.com/flysolo17/crop_samarica/refs/heads/main/Images/Rice%20black%20bug.jpg"
    ),
    title = "Rice Black Bug – Pest".localize(),
    description = "Rice black bugs (Scotinophara spp.) suck sap from rice stems, causing tiller death and poor growth. They are nocturnal and hide during the day. Populations increase in poorly managed fields with continuous planting and abundant weeds. They often move in swarms.".localize(),
    symptoms = listOf(
        "Wilting and yellowing of leaves",
        "Deadheart in seedlings and tillers",
        "Presence of black shield-shaped insects at plant base",
        "Circular patches of damaged rice in the field"
    ).localize(),
    prevention = listOf(
        Prevention(
            "Cultural and Physical Controls".localize(),
            listOf(
                "Practice synchronous planting and crop rotation",
                "Plow and flood fields after harvest to kill bugs",
                "Use light traps to monitor and reduce populations"
            ).localize()
        ),
        Prevention(
            "Biological Control".localize(),
            listOf("Conserve natural enemies such as fungi and egg parasitoids").localize()
        ),
        Prevention(
            "Chemical Control (if necessary)".localize(),
            listOf("Apply insecticides only when infestations are severe").localize()
        )
    )
)
