//
//  FacebookHelper.h
//  HauteCouture
//
//  Created by 尚 光泉 on 13-9-5.
//
//

#import <Foundation/Foundation.h>

@interface FacebookHelper : NSObject{
    UIAlertView * loadingAlert;
}

-(void)share:(const char *)shareMessage;

@end
