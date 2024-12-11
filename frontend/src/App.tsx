import { useState, useEffect } from 'react';
import './App.css';
import 'leaflet/dist/leaflet.css';
import axios from 'axios';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { LatLng } from 'leaflet';
import L from 'leaflet';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

L.Icon.Default.prototype.options.iconUrl = markerIcon;
L.Icon.Default.prototype.options.iconRetinaUrl = markerIcon2x;
L.Icon.Default.prototype.options.shadowUrl = markerShadow;

const App = () => {
  const [markers, setMarkers] = useState<any[]>([]);
  const [selectedMarker, setSelectedMarker] = useState<any | null>(null); // 保存当前选中的标注点
  const [selectedTab, setSelectedTab] = useState<string>('传承人'); // 默认选中的标签
  const [loading, setLoading] = useState<boolean>(false); // 加载状态
  const [error, setError] = useState<string | null>(null); // 错误状态

  // 根据标签决定请求的API接口URL
  const endpointMap: Record<string, string> = {
    '传承人': '/api/inheritors',
    '非物质文化遗产项目': '/api/projects',
    '保护单位': '/api/protection-units'
  };

  useEffect(() => {
    const fetchMarkers = async () => {
      setLoading(true);
      setError(null);
      try {
        const url = endpointMap[selectedTab];
        const response = await axios.get(url);
        const data = response.data;

        // data应为一个数组，其中每个对象都包含latitude和longitude字段(假设后端已经实现)
        const locations = data.map((item: any) => {
          const latitude = item.latitude;
          const longitude = item.longitude;
          if (latitude && longitude) {
            return {
              position: new LatLng(latitude, longitude),
              details: item,
            };
          }
          return null;
        });

        const validLocations = locations.filter((loc: any) => loc !== null);
        setMarkers(validLocations);
      } catch (error) {
        console.error(`Error fetching ${endpointMap[selectedTab]}:`, error);
        setError('数据获取出错');
      } finally {
        setLoading(false);
      }
    };

    fetchMarkers();
  }, [selectedTab]);

  return (
    <div className="app">
      {/* 工具栏卡片 */}
      <Card className="absolute top-4 right-4 z-[1000] w-80">
        <CardHeader>
          <CardTitle>工具栏</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Tabs 组件 */}
          <Tabs value={selectedTab} onValueChange={(value) => setSelectedTab(value)}>
            <TabsList>
              <TabsTrigger value="传承人">传承人</TabsTrigger>
              <TabsTrigger value="非物质文化遗产项目">非物质文化遗产项目</TabsTrigger>
              <TabsTrigger value="保护单位">保护单位</TabsTrigger>
            </TabsList>
          </Tabs>

          {/* 搜索输入 */}
          <Input placeholder="搜索..." />

          {/* 切换侧边栏按钮 */}
          <div className="space-x-4">
            <Button onClick={() => { /* 切换侧边栏逻辑 */ }}>切换侧边栏</Button>
          </div>

          {/* 加载指示器 */}
          {loading && <p>加载中...</p>}

          {/* 错误信息 */}
          {error && <p className="text-red-500">{error}</p>}

          {/* 展示选中标注点的详细信息 */}
          {selectedMarker && (
            <div className="mt-4">
              <h3 className="text-lg font-bold">详细信息</h3>
              {selectedTab === '非物质文化遗产项目' && (
                <>
                  <p><strong>项目名称:</strong> {selectedMarker.details.projectName}</p>
                  <p><strong>分类:</strong> {selectedMarker.details.category}</p>
                  <p><strong>公布日期:</strong> {selectedMarker.details.announcementDate}</p>
                  <p><strong>类型:</strong> {selectedMarker.details.type}</p>
                  <p><strong>区域:</strong> {selectedMarker.details.applicationRegion}</p>
                </>
              )}

              {selectedTab === '传承人' && (
                <>
                  <p><strong>姓名:</strong> {selectedMarker.details.name}</p>
                  <p><strong>性别:</strong> {selectedMarker.details.gender}</p>
                  <p><strong>民族:</strong> {selectedMarker.details.ethnicity}</p>
                  <p><strong>类别:</strong> {selectedMarker.details.category}</p>
                  <p><strong>关联项目ID:</strong> {selectedMarker.details.projectID}</p>
                </>
              )}

              {selectedTab === '保护单位' && (
                <>
                  <p><strong>单位名称:</strong> {selectedMarker.details.unitName}</p>
                  <p><strong>区域:</strong> {selectedMarker.details.region}</p>
                  <p><strong>联系方式:</strong> {selectedMarker.details.contactInfo}</p>
                </>
              )}
            </div>
          )}
        </CardContent>
      </Card>

      {/* 地图 */}
      <MapContainer
        className="map"
        center={[51.0, 19.0]}
        zoom={4}
        maxZoom={18}
        style={{ height: '100vh' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; OpenStreetMap contributors'
        />
        {markers.map((marker, idx) => (
          <Marker
            key={`marker-${idx}`}
            position={marker.position}
            eventHandlers={{
              click: () => {
                setSelectedMarker(marker);
                console.log(marker);
                
              },
            }}
          >
            <Popup>
              {/* 根据不同类型显示相应的名称字段 */}
              {selectedTab === '非物质文化遗产项目' && <strong>{marker.details.projectName}</strong>}
              {selectedTab === '传承人' && <strong>{marker.details.name}</strong>}
              {selectedTab === '保护单位' && <strong>{marker.details.unitName}</strong>}
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default App;