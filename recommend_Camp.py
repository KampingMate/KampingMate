import sys
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from surprise import Dataset, Reader, SVD, accuracy
from surprise.model_selection import train_test_split

def filter_camp_data(camp, selected_faclt, selected_lct, selected_induty, selected_bottom, selected_sbrs):
    
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

def create_combined_features(camp):
    features = camp[['DO_NM', 'FACLT_DIV_NM', 'INDUTY', 'LCT_CL', 'OPER_PD_CL', 'SBRS_CL']]
    combined_features = features.fillna('').apply(lambda x: ' '.join(x), axis=1)
    return combined_features

def get_cosine_similar_items(user_rated_camps, camp_data):
    vectorizer = TfidfVectorizer()
    combined_features = create_combined_features(camp_data)
    tfidf_matrix = vectorizer.fit_transform(combined_features)
    
    cosine_similarities = np.zeros(tfidf_matrix.shape[0])
    for camp in user_rated_camps:
        user_query_vector = vectorizer.transform([camp])
        cosine_similarities += cosine_similarity(user_query_vector, tfidf_matrix).flatten()
    
    cosine_similarities /= len(user_rated_camps)
    camp_data = camp_data.copy()
    camp_data.loc[:, 'cosine_similarity'] = cosine_similarities
    return camp_data.sort_values(by='cosine_similarity', ascending=False)

if __name__ == "__main__":
    sys.stdout.reconfigure(encoding='utf-8')
     
    # 데이터 로드
    file_path = 'E:/Student/API/GoCamping.pkl'
    camp = pd.read_pickle(file_path)
    
    selected_do = sys.argv[1]
    selected_faclt = sys.argv[2]
    selected_lct = sys.argv[3]
    selected_induty = sys.argv[4]
    selected_bottom = sys.argv[5]
    selected_sbrs = sys.argv[6]
    # 사용자 ID 설정
    user_id = sys.argv[7]

    # member_rate 데이터 로드
    member_rate = pd.read_pickle('E:/Student/API/memberRate.pkl')

    # 사용자가 RATING을 부여한 모든 CONTENT_ID 추출
    user_rated_camps = member_rate[member_rate['NO_DATA'] == int(user_id)]['CONTENT_ID'].tolist()
    user_rated_camps_names = create_combined_features(camp[camp['CONTENT_ID'].isin(user_rated_camps)]).values.tolist()

    # 코사인 유사도 기반 추천 (전체 데이터에서 1차 필터링)
    all_camps_filtered = get_cosine_similar_items(user_rated_camps_names, camp)

    # 필터 조건 적용 (1차 필터링된 데이터에서 2차 필터링)
    filtered_camp = filter_camp_data(all_camps_filtered, selected_faclt, selected_lct, selected_induty, selected_bottom, selected_sbrs)

    # 필터링된 캠핑장의 content_id 추출
    content_ids = filtered_camp['CONTENT_ID'].tolist()

    # 필터링된 content_id에 해당하는 rating 데이터 필터링
    filtered_ratings = member_rate[member_rate['CONTENT_ID'].isin(content_ids)]

    # surprise용 데이터 준비
    reader = Reader(rating_scale=(1, 5))
    data = Dataset.load_from_df(filtered_ratings[['NO_DATA', 'CONTENT_ID', 'RATING']], reader)

    try:
        # train-test split
        if len(filtered_ratings) > 1:
            trainset, testset = train_test_split(data, test_size=0.25)
        else:
            raise ValueError("Not enough data to split into train and test sets")

        # 최적의 하이퍼파라미터를 사용하여 SVD 모델 학습
        svd = SVD(n_factors=50, n_epochs=20, lr_all=0.005, reg_all=0.2)
        svd.fit(trainset)

        # 예측 및 평가
        predictions = svd.test(testset)

        # 추천 결과 출력
        user_ratings = filtered_ratings[filtered_ratings['NO_DATA'] == int(user_id)]
        user_unrated_items = [item for item in content_ids if item not in user_ratings['CONTENT_ID'].tolist()]

        # 추천 항목 생성
        recommendations = []
        for item_id in user_unrated_items:
            est_rating = svd.predict(user_id, item_id).est
            recommendations.append((item_id, est_rating))

        recommendations.sort(key=lambda x: x[1], reverse=True)

        # 상위 추천 항목 출력
        top_recommendations = recommendations[:10]
        for item_id, rating in top_recommendations:
            print(f"{item_id}")
    except ValueError as e:
        print("검색된 조건으로 0건의 추천결과가 있습니다.")
