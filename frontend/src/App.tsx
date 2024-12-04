import { useState } from 'react';
import './App.css';
import 'leaflet/dist/leaflet.css';
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Drawer } from '@/components/ui/drawer';
import { LatLng } from 'leaflet';
import L, { LeafletMouseEvent } from 'leaflet';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

L.Icon.Default.prototype.options.iconUrl = markerIcon;
L.Icon.Default.prototype.options.iconRetinaUrl = markerIcon2x;
L.Icon.Default.prototype.options.shadowUrl = markerShadow;

const App = () => {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [markers, setMarkers] = useState<LatLng[]>([]);
  const [addingMarker, setAddingMarker] = useState(false);

  const toggleDrawer = () => {
    setDrawerOpen(!drawerOpen);
  };

  const handleMapClick = (e: LeafletMouseEvent) => {
    if (addingMarker) {
      setMarkers([...markers, e.latlng]);
      setAddingMarker(false);
    }
  }
  const MapClickHandler = () => {
    useMapEvents({
      click: handleMapClick,
    });
    return null;
  };

  return (
    <div className="app">
      <Card className="absolute top-4 right-4 z-[1000] w-72">
        <CardHeader>
          <CardTitle>工具栏</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <Input placeholder="搜索..." />
          <div className='space-x-4'>
            <Button onClick={() => setAddingMarker(true)}>添加点</Button>
            <Button onClick={toggleDrawer}>详细信息</Button>
          </div>
        </CardContent>
      </Card>
      <Drawer open={drawerOpen} onClose={toggleDrawer}>
      </Drawer>
      <MapContainer
        className="map"
        center={[51.0, 19.0]}
        zoom={4}
        maxZoom={18}
        style={{ height: '100vh' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        />
        {markers.map((position, idx) => (
          <Marker key={`marker-${idx}`} position={position} />
        ))}
        <MapClickHandler />
      </MapContainer>
    </div>
  );
};

export default App;