// MarkerDetails.tsx
import React from 'react';
import { Button } from '@/components/ui/button';

interface MarkerDetailsProps {
  selectedTab: string;
  details: any;
}
import { useGlobalStore } from "@/lib/store";
import axios from 'axios';

const MarkerDetails: React.FC<MarkerDetailsProps> = ({ selectedTab, details }) => {
  const setFormVisible = useGlobalStore((state) => state.setFormVisible);
  const handleDelete = () => {

    console.log('删除');
    axios.delete(`/api/${selectedTab}/${details.projectId}`).then(() => {
      console.log('删除成功');
    });
  }

  if (!details) return null;
  return (
    <>
    <div className="mt-4">
      <h3 className="text-lg font-bold">详细信息</h3>
      {selectedTab === 'projects' && (
        <>
          <p><strong>项目名称:</strong> {details.projectName}</p>
          <p><strong>分类:</strong> {details.category}</p>
          <p><strong>公布日期:</strong> {details.announcementDate}</p>
          <p><strong>类型:</strong> {details.type}</p>
          <p><strong>区域:</strong> {details.applicationRegion}</p>
        </>
      )}

      {selectedTab === 'inheritors' && (
        <>
          <p><strong>姓名:</strong> {details.name}</p>
          <p><strong>性别:</strong> {details.gender}</p>
          <p><strong>民族:</strong> {details.ethnicity}</p>
          <p><strong>类别:</strong> {details.category}</p>
          <p><strong>关联项目ID:</strong> {details.projectID}</p>
        </>
      )}

      {selectedTab === 'protection-units' && (
        <>
          <p><strong>单位名称:</strong> {details.unitName}</p>
          <p><strong>区域:</strong> {details.region}</p>
          <p><strong>联系方式:</strong> {details.contactInfo}</p>
        </>
      )}
    </div>
<div className="flex gap-4 mt-6">
<Button className='w-full' variant='secondary' onClick={() => {setFormVisible(true)}}>添加</Button>
        <Button
          type="button"
          variant="secondary"
          className="w-full"
        >
        修改
        </Button>
        <Button 
          variant="default"
          onClick={handleDelete}
          className="w-full bg-red-500 hover:bg-red-600 text-white"
        >
          删除
        </Button>
      </div>
    </>
  );
};

export default MarkerDetails;