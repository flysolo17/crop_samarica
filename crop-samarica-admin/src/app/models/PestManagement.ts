export interface PestManagement {
  id: string;
  images: string[];
  stages: RiceStage[];
  title: string;
  description: string;
  symptoms: string[];
  prevention: Prevention[];
}

export interface Prevention {
  title: string;
  text: string[];
}

export type RiceStage = string;

export const RiceStages = {
  SEEDLING: 'SEEDLING',
  TILLERING: 'TILLERING',
  STEM_ELONGATION: 'STEM_ELONGATION',
  PANICLE_INITIATION: 'PANICLE_INITIATION',
  BOOTING: 'BOOTING',
  FLOWERING: 'FLOWERING',
  MILKING: 'MILKING',
  DOUGH: 'DOUGH',
  MATURE: 'MATURE',
} as const;
