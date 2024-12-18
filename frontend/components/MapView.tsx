// MapView.tsx
import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import { useGlobalStore } from "@/lib/store";

// 设置默认图标
L.Icon.Default.prototype.options.iconUrl = markerIcon;
L.Icon.Default.prototype.options.iconRetinaUrl = markerIcon2x;
L.Icon.Default.prototype.options.shadowUrl = markerShadow;

interface MapViewProps {
  markers: {
    position: { lat: number, lng: number };
    details: any;
  }[];
  selectedTab: string;
  onMarkerClick: (marker: any) => void;
}

const MapView: React.FC<MapViewProps> = ({ markers, selectedTab, onMarkerClick }) => {
  const [localMarkers, setLocalMarkers] = useState<typeof markers>([]);
  useEffect(() => {
    // 合并 API 获取的 markers 和本地添加的 markers
    const existingLocalMarkers = localMarkers.filter(localMarker => 
      !markers.some(marker => 
        marker.position.lat === localMarker.position.lat && 
        marker.position.lng === localMarker.position.lng
      )
    );
    setLocalMarkers([...markers, ...existingLocalMarkers]);
  }, [markers]);
  const setNewPosition = useGlobalStore((state) => state.setNewPosition);
  const isSelecting = useGlobalStore((state) => state.isSelecting);
  const toggleSelecting = useGlobalStore((state) => state.toggleSelecting);
  const handleMapClick = (e: L.LeafletMouseEvent) => {
    if (isSelecting) {
      setLocalMarkers(prev => [...prev, {position: e.latlng, details: {}}]);
      setNewPosition(e.latlng);
      toggleSelecting();
    }
  }
  const MapClickHandler = () => {
    useMapEvents({
      click: handleMapClick,
    });
    return null;
  }

  return (
    <MapContainer
      className="map"
      center={[34.34, 108.95]}
      zoom={4}
      maxZoom={18}
      style={{ height: '100vh' }}
    >
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; OpenStreetMap contributors'
      />
      {localMarkers.map((marker, idx) => (
        <Marker
          key={`marker-${idx}`}
          position={marker.position}
          eventHandlers={{
            click: () => {
              onMarkerClick(marker);
            },
          }}
        >
          <Popup>
            {selectedTab === 'projects' && <strong>{marker.details.projectName}</strong>}
            {selectedTab === 'inheritors' && <strong>{marker.details.name}</strong>}
            {selectedTab === 'protection-units' && <strong>{marker.details.unitName}</strong>}
          </Popup>
        </Marker>
      ))}
      <MapClickHandler />
    </MapContainer>
  );
};

export default MapView;