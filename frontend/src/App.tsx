import { useState } from "react";
import Toolbar from "@/components/Toolbar";
import MarkerDetails from "@/components/MarkerDetails";
import MapView from "@/components/MapView";
import { useMarkers } from "@/components/useMarkers";
import "./App.css";
// import { useGlobalStore } from "@/lib/store";
export default function HomePage() {
  const [selectedTab, setSelectedTab] = useState<string>("inheritors");
  const { markers, loading, error } = useMarkers(selectedTab);
  const [selectedMarker, setSelectedMarker] = useState<any | null>(null);
  // const isSelecting = useGlobalStore((state) => state.isSelecting);
  // const toggleSelecting = useGlobalStore((state) => state.toggleSelecting);
  return (
    <div className="app">
      <Toolbar
        selectedTab={selectedTab}
        onTabChange={setSelectedTab}
        loading={loading}
        error={error}
      >
        {selectedMarker && (
          <MarkerDetails
            selectedTab={selectedTab}
            details={selectedMarker.details}
          />
        )}
      </Toolbar>
      {/* <div>
      <p>Selecting is: {isSelecting ? 'On' : 'Off'}</p>
      <button onClick={toggleSelecting}>Toggle Selecting</button>
    </div> */}
      <MapView
        markers={markers}
        selectedTab={selectedTab}
        onMarkerClick={(marker) => {
          setSelectedMarker(marker);
          console.log(marker);
        }}
      />
    </div>
  );
}

