// store.ts
import { LatLng } from 'leaflet';
import {create} from 'zustand';

interface GlobalStore {
  isSelecting: boolean;
  toggleSelecting: () => void;
  newPosition: LatLng | null;
  setNewPosition: (newPosition: LatLng) => void;
}

export const useGlobalStore = create<GlobalStore>((set) => ({
  isSelecting: false,
  toggleSelecting: () => set((state) => ({ isSelecting: !state.isSelecting })),
  newPosition: null,
  setNewPosition: (newPosition: LatLng) => set({ newPosition }),
}));