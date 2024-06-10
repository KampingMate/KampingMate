import sys
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer

def filter_camp_data(camp, selected_do, selected_gu, selected_faclt, selected_lct, selected_induty, selected_bottom, selected_sbrs):
    # 선택된 값을 포함하는 행만 필터링, 빈 문자열 무시
    if selected_do:
        camp = camp[camp['DO_NM'].str.contains(selected_do, na=False)]
    if selected_gu:
        camp = camp[camp['SIGUNGU_NM'].str.contains(selected_gu, na=False)]
    if selected_faclt:
        camp = camp[camp['FACLT_DIV_NM'].str.contains(selected_faclt, na=False)]
    if selected_lct:
        camp = camp[camp['LCT_CL'].str.contains(selected_lct, na=False)]
    if selected_induty:
        camp = camp[camp['INDUTY'].str.contains(selected_induty, na=False)]
    if selected_bottom:
        bottom_mask = None
        if selected_bottom == '잔디':
            bottom_mask = camp['SITE_BOTTOM_CL1'].fillna(0) > 0
        elif selected_bottom == '파쇄석':
            bottom_mask = camp['SITE_BOTTOM_CL2'].fillna(0) > 0
        elif selected_bottom == '테크':
            bottom_mask = camp['SITE_BOTTOM_CL3'].fillna(0) > 0
        elif selected_bottom == '자갈':
            bottom_mask = camp['SITE_BOTTOM_CL4'].fillna(0) > 0
        elif selected_bottom == '맨흙':
            bottom_mask = camp['SITE_BOTTOM_CL5'].fillna(0) > 0
        if bottom_mask is not None:
            camp = camp[bottom_mask]
    if selected_sbrs:
        camp = camp[camp['SBRS_CL'].str.contains(selected_sbrs, na=False)]

    return camp

if __name__ == "__main__":
    # 데이터 로드
    file_path = 'E:/Student/API/GoCamping.pkl'
    camp = pd.read_pickle(file_path)
    
    selected_do = sys.argv[1]
    selected_gu = sys.argv[2]
    selected_faclt = sys.argv[3]
    selected_lct = sys.argv[4]
    selected_induty = sys.argv[5]
    selected_bottom = sys.argv[6]
    selected_sbrs = sys.argv[7]

    filtered_camp = filter_camp_data(camp, selected_do, selected_gu, selected_faclt, selected_lct, selected_induty, selected_bottom, selected_sbrs)
    
    # 결과 확인
    sys.stdout.reconfigure(encoding='utf-8')
    print(filtered_camp.to_json(orient='records', force_ascii=False))
