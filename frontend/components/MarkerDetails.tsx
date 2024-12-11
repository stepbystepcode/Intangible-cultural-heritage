// MarkerDetails.tsx
import React from 'react';

interface MarkerDetailsProps {
  selectedTab: string;
  details: any;
}

const MarkerDetails: React.FC<MarkerDetailsProps> = ({ selectedTab, details }) => {
  if (!details) return null;
  return (
    <div className="mt-4">
      <h3 className="text-lg font-bold">详细信息</h3>
      {selectedTab === '非物质文化遗产项目' && (
        <>
          <p><strong>项目名称:</strong> {details.projectName}</p>
          <p><strong>分类:</strong> {details.category}</p>
          <p><strong>公布日期:</strong> {details.announcementDate}</p>
          <p><strong>类型:</strong> {details.type}</p>
          <p><strong>区域:</strong> {details.applicationRegion}</p>
        </>
      )}

      {selectedTab === '传承人' && (
        <>
          <p><strong>姓名:</strong> {details.name}</p>
          <p><strong>性别:</strong> {details.gender}</p>
          <p><strong>民族:</strong> {details.ethnicity}</p>
          <p><strong>类别:</strong> {details.category}</p>
          <p><strong>关联项目ID:</strong> {details.projectID}</p>
        </>
      )}

      {selectedTab === '保护单位' && (
        <>
          <p><strong>单位名称:</strong> {details.unitName}</p>
          <p><strong>区域:</strong> {details.region}</p>
          <p><strong>联系方式:</strong> {details.contactInfo}</p>
        </>
      )}
    </div>
  );
};

export default MarkerDetails;